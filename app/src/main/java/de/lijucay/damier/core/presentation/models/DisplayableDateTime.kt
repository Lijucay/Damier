package de.lijucay.damier.core.presentation.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class DisplayableDateTime(
    val formatted: String,
    val value: LocalDateTime
)

fun LocalDateTime.toDisplayableDateTime(): DisplayableDateTime {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL)

    return DisplayableDateTime(
        formatted = this.format(formatter),
        value = this
    )
}