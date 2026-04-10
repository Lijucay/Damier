package de.lijucay.damier.activity_details.presentation

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.DisplayableDateTime
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import java.time.LocalDateTime

data class CheckInFormState(
    val dateTime: DisplayableDateTime = LocalDateTime.now().toDisplayableDateTime(),
    val amount: TextFieldValue = TextFieldValue("1", selection = TextRange(1)),
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false
) {
    val isSaveEnabled: Boolean get() = amount.text.toIntOrNull().let { it != null && it > 0 }

    companion object {
        fun fromExisting(checkIn: CheckInUi) = CheckInFormState(
            dateTime = checkIn.dateTime,
            amount = checkIn.amount.toString().let {
                TextFieldValue(it, selection = TextRange(it.length))
            },
            showDatePicker = false,
            showTimePicker = false
        )
    }
}