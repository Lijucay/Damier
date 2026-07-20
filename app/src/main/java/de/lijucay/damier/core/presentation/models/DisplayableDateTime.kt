package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.converter.LocalDateTimeSerializer
import de.lijucay.damier.core.data.entities.CheckInInfo
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Serializable
data class DisplayableDateTime(
    val formatted: String,
    @Serializable(with = LocalDateTimeSerializer::class) val value: LocalDateTime
)

fun LocalDateTime.toDisplayableDateTime(): DisplayableDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, HH:mm:ss", Locale.getDefault())

    return DisplayableDateTime(
        formatted = this.format(formatter),
        value = this
    )
}

fun LocalDateTime.toDisplayableDateTimeWithMs(): DisplayableDateTime {
    val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy, HH:mm:ss.SSS", Locale.getDefault())

    return DisplayableDateTime(
        formatted = this.format(formatter),
        value = this
    )
}

fun CheckInInfo.toCheckInUi(): CheckInUi {
    return CheckInUi(
        id = id,
        activityId = activityId,
        dateTime = timestamp.toDisplayableDateTime(),
        amount = amount
    )
}