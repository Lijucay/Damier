package de.lijucay.damier.activity.presentation

import androidx.lifecycle.ViewModel
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.core.presentation.getRandomCheckInInfo
import de.lijucay.damier.activity.domain.ActivityFormState
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import java.util.UUID

class ActivityFormViewModel : ViewModel() {
    private val _state = MutableStateFlow(ActivityFormState())
    val state = _state.asStateFlow()

    private var activityId: UUID = UUID.randomUUID()

    fun initForAdd() {
        activityId = UUID.randomUUID()
        _state.value = ActivityFormState(checkInInfo = getRandomCheckInInfo())
    }

    fun initForEdit(activity: ActivityUi) {
        activityId = activity.id
        _state.value = ActivityFormState.fromExisting(activity)
    }

    fun setTitle(value: String) = _state.update { it.copy(title = value) }

    fun setUnitId(value: UnitId) = _state.update { it.copy(unitId = value) }

    fun setDefaultAmount(value: Int) = _state.update { it.copy(defaultAmount = value) }

    fun setReference(value: Int) = _state.update { state ->
        state.copy(
            reference = value,
            checkInInfo = state.checkInInfo?.copy(reference = value)
        )
    }

    fun setReferenceType(value: ReferenceType) = _state.update { state ->
        state.copy(
            referenceType = value,
            checkInInfo = state.checkInInfo?.copy(referenceType = value)
        )
    }

    fun setRemindersEnabled(enabled: Boolean) = _state.update { state ->
        state.copy(remindersEnabled = enabled)
    }

    fun setReminderTime(time: LocalTime) = _state.update { state ->
        state.copy(reminderTime = time)
    }

    fun buildActivityInfo(): ActivityInfo {
        val s = _state.value
        return ActivityInfo(
            id = activityId,
            activityName = s.title,
            unit = s.unitId,
            reference = s.reference,
            referenceType = s.referenceType,
            defaultAmount = s.defaultAmount,
            remindersEnabled = s.remindersEnabled,
            reminderTime = s.reminderTime
        )
    }

    fun setShowUnitSelectionSheet(show: Boolean) = _state.update { it.copy(showUnitsSelectionDialog = show) }
    fun setShowNotificationInfoDialog(show: Boolean) = _state.update { it.copy(showNotificationDialog = show) }
}