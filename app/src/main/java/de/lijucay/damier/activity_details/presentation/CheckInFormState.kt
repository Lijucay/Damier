package de.lijucay.damier.activity_details.presentation

import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.DisplayableDateTime
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import java.time.LocalDateTime

data class CheckInFormState(
    val dateTime: DisplayableDateTime = LocalDateTime.now().toDisplayableDateTime(),
    val amount: Int = 1,
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
) {
    val isSaveEnabled: Boolean get() = amount > 0

    companion object {
        fun fromExisting(checkIn: CheckInUi) = CheckInFormState(
            dateTime = checkIn.dateTime,
            amount = checkIn.amount,
            showDatePicker = false,
            showTimePicker = false
        )
    }
}