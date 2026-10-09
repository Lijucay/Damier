package de.lijucay.damier.activity.data.wrapper

import de.lijucay.damier.activity.data.model.Activity
import de.lijucay.damier.check_in.data.wrapper.toCheckInDomain
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.streak.data.wrapper.toStreakDomain
import de.lijucay.damier.activity.domain.ActivityDomain
import de.lijucay.damier.nfc.data.wrapper.toNfcChipDomains

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
        nfcChipId = chips.toNfcChipDomains(),
        remindersEnabled = activityInfo.remindersEnabled,
        reminderTime = activityInfo.reminderTime
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
        remindersEnabled = remindersEnabled,
        reminderTime = reminderTime
    )
}