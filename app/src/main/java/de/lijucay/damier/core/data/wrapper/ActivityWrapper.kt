package de.lijucay.damier.core.data.wrapper

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.domain.models.ActivityDomain

/**
 * Maps the Room relation POJO [Activity] to the pure domain model [ActivityDomain].
 */
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
        nfcChipId = activityInfo.nfcChipId
    )
}

fun List<Activity>.toActivityDomains(): List<ActivityDomain> = this.map { it.toActivityDomain() }

/**
 * Reverse mapping for the write path (e.g. ActivityFormViewModel saving an edited activity).
 * */
fun ActivityDomain.toActivityInfo(): ActivityInfo {
    return ActivityInfo(
        id = id,
        activityName = title,
        unit = unitId,
        reference = reference,
        referenceType = referenceType,
        defaultAmount = defaultAmount,
        nfcChipId = nfcChipId
    )
}