package de.lijucay.damier.activity_details.presentation

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.cue_write.NfcWriteManager
import de.lijucay.cue_write.WriteResult
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.cue.NfcWriteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class ActivityDetailsViewModel(
    private val nfcWriteManager: NfcWriteManager,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityDetailsState())
    val state = _state.asStateFlow()

    fun load(activity: ActivityUi) {
        _state.update { current ->
            ActivityDetailsState.fromActivityUi(activity, LocalDate.now())
                .copy(
                    showCheckInHistory = current.showCheckInHistory
                )
        }
    }

    fun clear() {
        _state.value = ActivityDetailsState()
    }

    fun setCheckInFormMode(mode: CheckInFormMode?) {
        _state.update {
            it.copy(checkInFormMode = mode)
        }
    }

    fun setShowStatsDialog(show: Boolean) {
        _state.update {
            it.copy(showStatsDialog = show)
        }
    }

    fun setShowHistory(show: Boolean) {
        _state.update { it.copy(showCheckInHistory = show) }
    }

    fun startNfcWrite() {
        _state.update { it.copy(nfcWriteState = NfcWriteState.WaitingForTag) }
    }

    fun dismissNfcWrite() {
        _state.update { it.copy(nfcWriteState = NfcWriteState.Idle) }
    }

    fun onTagDiscovered(tag: Tag, activityId: UUID) {
        if (_state.value.nfcWriteState !is NfcWriteState.WaitingForTag) return
        _state.update { it.copy(nfcWriteState = NfcWriteState.Writing) }

        viewModelScope.launch(Dispatchers.IO) {
            val (result, chip) = nfcWriteManager.write(tag, "damier")

            if (result == WriteResult.Success && chip != null) {
                activityRepository.updateNfcChipId(activityId, chip.chipId)
            }

            _state.update {
                it.copy(
                    nfcWriteState = when (result) {
                        WriteResult.Success -> NfcWriteState.Success(chip!!.chipId)
                        WriteResult.TagLost -> NfcWriteState.TagLost
                        WriteResult.NotWriteable -> NfcWriteState.NotWriteable
                        WriteResult.InsufficientSize -> NfcWriteState.InsufficientSize
                        WriteResult.NotNdefCompatible -> NfcWriteState.NotNdefCompatible
                        is WriteResult.UnknownError -> NfcWriteState.Unknown
                    }
                )
            }
        }
    }
}
