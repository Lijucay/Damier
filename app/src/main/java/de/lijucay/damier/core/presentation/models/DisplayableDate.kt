package de.lijucay.damier.core.presentation.models

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class DisplayableDate(
    val formatted: String,
    val value: LocalDate
)

fun LocalDate.toDisplayableDate(): DisplayableDate {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

    return DisplayableDate(
        formatted = this.format(formatter),
        value = this
    )
}
