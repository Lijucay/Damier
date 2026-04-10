package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.models.CheckInUi

fun CheckInUi.toCheckInInfo(): CheckInInfo {
    return CheckInInfo(
        id = id,
        activityId = activityId,
        timestamp = dateTime.value,
        checkInCount = amount
    )
}