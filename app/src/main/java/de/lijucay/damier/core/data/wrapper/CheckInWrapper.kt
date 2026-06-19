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

//fun CheckInUi.toCheckInInfo(): CheckInInfo {
//    return CheckInInfo(
//        id = id,
//        activityId = activityId,
//        timestamp = dateTime.value,
//        amount = amount
//    )
//}
//
//fun Map<LocalDate, List<CheckInUi>>.toScrollableChartEntries(
//    startDate: LocalDate,
//    today: LocalDate
//): List<Double> {
//    val result = mutableListOf<Double>()
//    var date = startDate
//    while (date <= today) {
//        result.add(this[date]?.sumOf { it.amount.toDouble() } ?: 0.0)
//        date = date.plusDays(1)
//    }
//    return result
//}

