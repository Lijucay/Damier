package de.lijucay.damier.nfc

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.nfc.read.NfcReadManager
import de.lijucay.damier.nfc.read.ReadResult
import de.lijucay.damier.nfc.write.NfcWriteManager
import de.lijucay.damier.nfc.write.WriteResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class NfcViewModel(
    private val nfcWriteManager: NfcWriteManager,
    private val nfcReadManager: NfcReadManager,
    private val activityRepository: ActivityRepository,
    private val recentNfcWriteTracker: RecentNfcWriteTracker
) : ViewModel() {
    private val _nfcWriteState = MutableStateFlow<NfcWriteState>(NfcWriteState.Idle)
    val nfcWriteState = _nfcWriteState.asStateFlow()

    private val _nfcLabelState = MutableStateFlow(NfcLabelState())
    val nfcLabelState = _nfcLabelState.asStateFlow()

    fun startNfcWrite() {
        _nfcWriteState.value = NfcWriteState.WaitingForTag
    }

    fun dismissNfcWrite() {
        _nfcWriteState.value = NfcWriteState.Idle
    }

    fun onTagDiscovered(
        tag: Tag,
        host: String,
        activityId: UUID,
    ) {
        if (_nfcWriteState.value !is NfcWriteState.WaitingForTag) return

        _nfcWriteState.value = NfcWriteState.Writing

        viewModelScope.launch {
            val result = nfcWriteManager.write(tag, host)

            if (result is WriteResult.Success) {
                activityRepository.linkNfcChip(chipId = UUID.fromString(result.chipId), activityId = activityId)
                recentNfcWriteTracker.markWritten(result.chipId)

                _nfcLabelState.update { it.copy(currentNfcTagId = UUID.fromString(result.chipId)) }
            }

            _nfcWriteState.value = when (result) {
                WriteResult.InsufficientSize -> NfcWriteState.InsufficientSize
                WriteResult.NotNdefCompatible -> NfcWriteState.NotNdefCompatible
                WriteResult.NotWriteable -> NfcWriteState.NotWriteable
                is WriteResult.Success -> NfcWriteState.Success(result.chipId)
                WriteResult.TagLost -> NfcWriteState.TagLost
                is WriteResult.UnknownError -> NfcWriteState.Unknown
                else -> NfcWriteState.Unknown
            }
        }
    }

    fun updateLabel(label: String, chipId: UUID) {
        viewModelScope.launch {
            activityRepository.updateNfcChipLabel(label, chipId)
        }
    }

    fun hideLabelDialog() {
        _nfcLabelState.update { it.copy(currentLabel = "", currentNfcTagId = null) }
    }

    fun setUpdateLabel(id: UUID, label: String) {
        _nfcLabelState.update { it.copy(currentNfcTagId = id, currentLabel = label) }
    }

    fun eraseNfcChip(tag: Tag) {
        if (_nfcWriteState.value !is NfcWriteState.WaitingForTag) return

        _nfcWriteState.value = NfcWriteState.Erasing

        viewModelScope.launch {
            val readResult = nfcReadManager.read(tag)
            val chipId = (readResult as? ReadResult.Success)
                ?.let { runCatching { UUID.fromString(it.chipId) }.getOrNull() }

            val result = nfcWriteManager.erase(tag)

            if (result == WriteResult.EraseSuccess) {
                chipId?.let { activityRepository.unlinkNfcChip(it) }
            }

            _nfcWriteState.value = when (result) {
                WriteResult.EraseSuccess -> NfcWriteState.EraseSuccess
                is WriteResult.EraseException -> NfcWriteState.EraseException(result.e)
                else -> NfcWriteState.Unknown
            }
        }
    }
}