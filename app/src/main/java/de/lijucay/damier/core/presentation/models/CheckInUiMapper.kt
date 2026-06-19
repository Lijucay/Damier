package de.lijucay.damier.core.presentation.models

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

fun List<CheckInDomain>.toCheckInUis(): List<CheckInUi> = this.map { it.toCheckInUi() }

fun CheckInUi.toCheckInDomain(): CheckInDomain {
    return CheckInDomain(
        id = id,
        activityId = activityId,
        timestamp = dateTime.value,
        amount = amount
    )
}

fun List<CheckInUi>.toCheckInDomains(): List<CheckInDomain> = this.map { it.toCheckInDomain() }

fun Map<LocalDate, List<CheckInUi>>.toScrollableChartEntries(
    startDate: LocalDate,
    today: LocalDate
): List<Double> {
    val result = mutableListOf<Double>()
    var date = startDate
    while (date <= today) {
        result.add(this[date]?.sumOf { it.amount.toDouble() } ?: 0.0)
        date = date.plusDays(1)
    }

    return result
}