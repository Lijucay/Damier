package dev.lijucay.damier.util

import dev.lijucay.damier.data.local.model.TrackingInfo
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object Utils {
    const val DATABASE_SCHEME_VERSION = 1

    fun groupTrackingInfoByMonth(
        trackingInfoList: List<TrackingInfo>
    ): Map<YearMonth, List<TrackingInfo>> {
        return trackingInfoList
            .sortedByDescending { ti -> LocalDateTime.parse("${ti.date}T${ti.time}") }
            .groupBy { ti -> YearMonth.from(LocalDate.parse(ti.date)) }
            .toSortedMap(Comparator.reverseOrder())
    }

    fun prepareLastSevenDaysData(trackingInfoList: List<TrackingInfo>): List<Pair<String, Int>> {
        val today = LocalDate.now()
        return (0..6).map { daysAgo ->
            val date = today.minusDays(daysAgo.toLong())
            val count = trackingInfoList
                .filter { ti -> LocalDate.parse(ti.date) == date }
                .sumOf { it.count }
            Pair(date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).take(3), count)
        }.reversed()
    }
}