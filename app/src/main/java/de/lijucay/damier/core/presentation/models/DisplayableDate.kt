package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.converter.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Serializable
data class DisplayableDate(
    val formatted: String,
    @Serializable(with = LocalDateSerializer::class) val value: LocalDate
)

fun LocalDate.toDisplayableDate(): DisplayableDate {
    val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)

    return DisplayableDate(
        formatted = this.format(formatter),
        value = this
    )
}
