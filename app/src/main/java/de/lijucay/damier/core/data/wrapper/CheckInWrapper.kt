package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.models.CheckInDomain

fun CheckInInfo.toCheckInDomain(): CheckInDomain {
    return CheckInDomain(
        id = id,
        activityId = activityId,
        timestamp = timestamp,
        amount = amount
    )
}

fun CheckInDomain.toCheckInInfo(): CheckInInfo {
    return CheckInInfo(
        id = id,
        activityId = activityId,
        timestamp = timestamp,
        amount = amount
    )
}

