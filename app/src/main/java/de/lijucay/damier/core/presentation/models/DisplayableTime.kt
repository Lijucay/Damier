package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.converter.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class DisplayableTime(
    val formatted: String,
    @Serializable(with = LocalTimeSerializer::class) val value: LocalTime
)

fun LocalTime.toDisplayableTime(): DisplayableTime {
    val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    return DisplayableTime(
        formatted = this.format(formatter),
        value = this
    )
}