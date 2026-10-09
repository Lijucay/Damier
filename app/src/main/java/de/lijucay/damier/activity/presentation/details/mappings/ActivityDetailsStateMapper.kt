package de.lijucay.damier.activity.presentation.details.mappings

import de.lijucay.damier.activity.presentation.details.states.ActivityDetailsState
import de.lijucay.damier.check_in.presentation.CheckInGroupUi
import de.lijucay.damier.activity.domain.WaffleDiagramData
import de.lijucay.damier.streak.domain.currentStreakLength
import de.lijucay.damier.streak.domain.longestStreakLength
import de.lijucay.damier.activity.presentation.ActivityUi
import de.lijucay.damier.check_in.presentation.toScrollableChartEntries
import de.lijucay.damier.core.presentation.toDayOfWeekDistribution
import java.time.DayOfWeek
import java.time.LocalDate

object ActivityDetailsStateMapper {
    fun map(activity: ActivityUi, today: LocalDate): ActivityDetailsState {
        val todaysCheckIns = activity
            .groupedCheckIns
            .find { it.date.value == today }
            ?.checkIns
            ?: emptyList()

        val chartStartDate = activity.groupedCheckIns.minOfOrNull { it.date.value } ?: today

        return ActivityDetailsState(
            title = activity.title,
            unitId = activity.unitId,
            referenceType = activity.referenceType,
            defaultAmount = activity.defaultAmount,
            allNfcChips = activity.nfcChipId,
            waffleDiagramData = WaffleDiagramData(
                reference = activity.reference,
                referenceType = activity.referenceType,
                checkIns = activity.groupedCheckIns.flatMap { it.checkIns }
            ),
            allCheckIns = activity.groupedCheckIns,
            streaks = activity.streaks,
            todaysCheckIns = todaysCheckIns,
            todaysTotal = todaysCheckIns.sumOf { it.amount },
            currentStreakLength = activity.streaks.currentStreakLength(today),
            longestStreakLength = activity.streaks.longestStreakLength(),
            weeklyComparison = buildWeeklyComparison(activity.groupedCheckIns, today),
            chartStartDate = chartStartDate,
            timelineEntries = activity.groupedCheckIns.toScrollableChartEntries(
                chartStartDate,
                today
            ),
            dayOfWeekDistribution = activity.groupedCheckIns.toDayOfWeekDistribution()
        )
    }

    private fun buildWeeklyComparison(
        groupedCheckIns: List<CheckInGroupUi>,
        today: LocalDate
    ): List<WeeklyComparisonEntry> {
        val monday = today.with(DayOfWeek.MONDAY)
        val lastMonday = monday.minusWeeks(1)

        return (0..6).map { offset ->
            val currentDate = monday.plusDays(offset.toLong())
            val lastWeekDate = lastMonday.plusDays(offset.toLong())

            val currentAmount = if (currentDate > today) 0.0
            else groupedCheckIns
                .find { it.date.value == currentDate }
                ?.checkIns
                ?.sumOf { it.amount.toDouble() }
                ?: 0.0

            val lastWeekAmount = groupedCheckIns
                .find { it.date.value == lastWeekDate }
                ?.checkIns
                ?.sumOf { it.amount.toDouble() }
                ?: 0.0

            WeeklyComparisonEntry(
                date = currentDate,
                currentAmount = currentAmount,
                lastWeekAmount = lastWeekAmount
            )
        }
    }
}