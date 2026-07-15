package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.domain.models.ActivityDomain

fun Activity.toActivityDomain(): ActivityDomain {
    return ActivityDomain(
        id = activityInfo.id,
        title = activityInfo.activityName,
        unitId = activityInfo.unit,
        reference = activityInfo.reference,
        referenceType = activityInfo.referenceType,
        defaultAmount = activityInfo.defaultAmount,
        checkIns = checkIns.map { it.toCheckInDomain() },
        streaks = streaks.map { it.toStreakDomain() },
        nfcChipId = chips.toNfcChipDomains()
    )
}

fun List<Activity>.toActivityDomains(): List<ActivityDomain> = this.map { it.toActivityDomain() }

fun ActivityDomain.toActivityInfo(): ActivityInfo {
    return ActivityInfo(
        id = id,
        activityName = title,
        unit = unitId,
        reference = reference,
        referenceType = referenceType,
        defaultAmount = defaultAmount,
    )
}