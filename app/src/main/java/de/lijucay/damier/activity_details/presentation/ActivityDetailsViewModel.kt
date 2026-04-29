package de.lijucay.damier.activity_details.presentation

import androidx.lifecycle.ViewModel
import de.lijucay.damier.core.presentation.models.ActivityUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class ActivityDetailsViewModel : ViewModel() {

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
}
