package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.entities.CheckInInfo
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class DisplayableDateTime(
    val formatted: String,
    val value: LocalDateTime
)

fun LocalDateTime.toDisplayableDateTime(): DisplayableDateTime {
    val formatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.FULL)
        .withZone(ZoneId.systemDefault())

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