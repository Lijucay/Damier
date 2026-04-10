package de.lijucay.damier.activity_details.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.util.UUID

class CheckInFormViewModel : ViewModel() {
    private val _state = MutableStateFlow(CheckInFormState())
    val state = _state.asStateFlow()

    private var checkInId: UUID = UUID.randomUUID()
    private var activityId: UUID = UUID.randomUUID()

    fun initForAdd(activityId: UUID) {
        checkInId = UUID.randomUUID()
        this.activityId = activityId
    }

    fun initForEdit(checkIn: CheckInUi) {
        checkInId = checkIn.id
        activityId = checkIn.activityId
        _state.value = CheckInFormState.fromExisting(checkIn)
    }

    fun setDateTime(dateTime: LocalDateTime) {
        val displayableDateTime = dateTime.toDisplayableDateTime()
        _state.update { it.copy(dateTime = displayableDateTime) }
    }

    fun setAmount(amount: TextFieldValue) = _state.update { it.copy(amount = amount) }

    fun buildCheckInInfo(): CheckInInfo {
        val s = _state.value
        return CheckInInfo(
            id = checkInId,
            activityId = activityId,
            timestamp = s.dateTime.value,
            checkInCount = s.amount.text.toInt()
        )
    }

    fun toggleShowDatePicker() {
        _state.update {
            it.copy(
                showDatePicker = !it.showDatePicker,
                showTimePicker = false
            )
        }
    }

    fun toggleShowTimePicker() {
        _state.update {
            it.copy(
                showDatePicker = false,
                showTimePicker = !it.showTimePicker
            )
        }
    }
}