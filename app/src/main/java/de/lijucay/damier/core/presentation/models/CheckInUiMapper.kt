package de.lijucay.damier.core.presentation.models

import de.lijucay.damier.core.data.CheckInGroupUi
import de.lijucay.damier.core.domain.models.CheckInDomain
import java.time.LocalDate

fun CheckInDomain.toCheckInUi(): CheckInUi {
    return CheckInUi(
        id = id,
        activityId = activityId,
        dateTime = timestamp.toDisplayableDateTime(),
        amount = amount
    )
}

fun CheckInUi.toCheckInDomain(): CheckInDomain {
    return CheckInDomain(
        id = id,
        activityId = activityId,
        timestamp = dateTime.value,
        amount = amount
    )
}

fun List<CheckInUi>.toCheckInDomains(): List<CheckInDomain> = this.map { it.toCheckInDomain() }

fun List<CheckInGroupUi>.toScrollableChartEntries(
    startDate: LocalDate,
    today: LocalDate
): List<Double> {
    val result = mutableListOf<Double>()
    var date = startDate
    while (date <= today) {
        result.add(find { it.date.value == date }?.checkIns?.sumOf { it.amount.toDouble() } ?: 0.0)
        date = date.plusDays(1)
    }

    return result
}