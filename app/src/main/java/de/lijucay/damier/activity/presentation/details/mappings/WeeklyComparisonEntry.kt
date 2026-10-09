package de.lijucay.damier.activity.presentation.details.mappings

import java.time.LocalDate

data class WeeklyComparisonEntry(
    val date: LocalDate,
    val currentAmount: Double,
    val lastWeekAmount: Double
)
