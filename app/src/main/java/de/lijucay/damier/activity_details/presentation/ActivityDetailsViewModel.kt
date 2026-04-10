package de.lijucay.damier.activity_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.presentation.models.ActivityUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

class ActivityDetailsViewModel(
    private val repository: ActivityRepository
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

    fun setShowHistory(show: Boolean) {
        _state.update { it.copy(showCheckInHistory = show) }
    }

    fun quickCheckIn(activity: ActivityUi) {
        viewModelScope.launch {
            repository.upsertCheckIn(
                CheckInInfo(
                    activityId = activity.id,
                    timestamp = LocalDateTime.now(),
                    checkInCount = activity.defaultAmount
                )
            )
        }
    }

    fun deleteCheckIn(checkIn: CheckInInfo) {
        viewModelScope.launch {
            repository.deleteCheckIn(checkIn)
        }
    }
}
