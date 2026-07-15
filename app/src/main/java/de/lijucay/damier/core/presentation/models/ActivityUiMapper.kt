package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.CheckInGroupUi
import de.lijucay.damier.core.domain.models.ActivityDomain

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
        nfcChipId = nfcChipId.toNfcChipUis()
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
        nfcChipId = nfcChipId.toNfcChipDomain()
    )
}