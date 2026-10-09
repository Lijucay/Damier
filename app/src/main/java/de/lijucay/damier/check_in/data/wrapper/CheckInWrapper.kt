package de.lijucay.damier.check_in.data.wrapper

import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import de.lijucay.damier.check_in.domain.model.CheckInDomain

fun CheckInInfo.toCheckInDomain(): CheckInDomain {
    return CheckInDomain(
        id = id,
        activityId = activityId,
        timestamp = timestamp,
        amount = amount,
        note = note
    )
}

fun CheckInDomain.toCheckInInfo(): CheckInInfo {
    return CheckInInfo(
        id = id,
        activityId = activityId,
        timestamp = timestamp,
        amount = amount,
        note = note
    )
}

