package de.lijucay.damier.core.presentation.models

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class DisplayableTime(
    val formatted: String,
    val value: LocalTime
)

fun LocalTime.toDisplayableTime(): DisplayableTime {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    return DisplayableTime(
        formatted = this.format(formatter),
        value = this
    )
}