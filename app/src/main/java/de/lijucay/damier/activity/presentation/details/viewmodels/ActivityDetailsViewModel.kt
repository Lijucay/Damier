package de.lijucay.damier.activity.presentation.details.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.activity.presentation.details.mappings.ActivityDetailsStateMapper
import de.lijucay.damier.activity.presentation.details.states.ActivityDetailsState
import de.lijucay.damier.check_in.presentation.CheckInFormMode
import de.lijucay.damier.activity.domain.ActivityRepository
import de.lijucay.damier.core.domain.DeletionMode
import de.lijucay.damier.activity.presentation.ActivityUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class ActivityDetailsViewModel(
    private val activityRepository: ActivityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ActivityDetailsState())
    val state = _state.asStateFlow()

    private val _deletionDialogMode = MutableStateFlow<DeletionMode?>(null)
    val deletionDialogMode = _deletionDialogMode.asStateFlow()

    fun load(activity: ActivityUi) {
        _state.update { current ->
            ActivityDetailsStateMapper.map(activity, LocalDate.now())
                .copy(
                    showCheckInHistory = current.showCheckInHistory,
                    showNfcList = current.showNfcList
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

    fun showMenu(show: Boolean) {
        _state.update { it.copy(menuExpanded = show) }
    }

    fun unlinkNfcChip(chipId: UUID) {
        viewModelScope.launch { activityRepository.unlinkNfcChip(chipId) }
    }


    fun setDeletionMode(deletionMode: DeletionMode?) {
        _deletionDialogMode.value = deletionMode
    }

    fun showNfcList(show: Boolean) {
        _state.update { it.copy(showNfcList = show) }
    }
}