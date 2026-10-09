package de.lijucay.damier.activity.presentation

import de.lijucay.damier.check_in.presentation.CheckInGroupUi
import de.lijucay.damier.activity.domain.ActivityDomain
import de.lijucay.damier.check_in.presentation.toCheckInDomains
import de.lijucay.damier.check_in.presentation.toCheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import de.lijucay.damier.core.presentation.models.toDisplayableTime
import de.lijucay.damier.nfc.presentation.toNfcChipDomain
import de.lijucay.damier.nfc.presentation.toNfcChipUis
import de.lijucay.damier.streak.presentation.toStreakDomains
import de.lijucay.damier.streak.presentation.toStreakUi

fun ActivityDomain.toActivityUi(): ActivityUi {
    val checkInUis = checkIns.map { it.toCheckInUi() }
    val groupedCheckIns = checkInUis
        .groupBy { it.dateTime.value.toLocalDate() }
        .map { (date, checkInsForDate) ->
            CheckInGroupUi(
                date = date.toDisplayableDate(),
                checkIns = checkInsForDate
            )
        }

        checkInUis.groupBy { it.dateTime.value.toLocalDate() }

    return ActivityUi(
        id = id,
        title = title,
        unitId = unitId,
        reference = reference,
        referenceType = referenceType,
        defaultAmount = defaultAmount,
        groupedCheckIns = groupedCheckIns,
        streaks = streaks.map { it.toStreakUi() },
        nfcChipId = nfcChipId.toNfcChipUis(),
        remindersEnabled = remindersEnabled,
        reminderTime = reminderTime?.toDisplayableTime()
    )
}

fun List<ActivityDomain>.toActivityUis() = this.map { it.toActivityUi() }

fun ActivityUi.toActivityDomain(): ActivityDomain {
    return ActivityDomain(
        id = id,
        title = title,
        unitId = unitId,
        reference = reference,
        referenceType = referenceType,
        defaultAmount = defaultAmount,
        checkIns = groupedCheckIns.flatMap { it.checkIns }.toCheckInDomains(),
        streaks = streaks.toStreakDomains(),
        nfcChipId = nfcChipId.toNfcChipDomain(),
        remindersEnabled = remindersEnabled,
        reminderTime = reminderTime?.value
    )
}