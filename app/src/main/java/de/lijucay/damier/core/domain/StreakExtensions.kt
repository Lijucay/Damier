package de.lijucay.damier.core.domain

import de.lijucay.damier.core.presentation.models.StreakUi
import java.time.LocalDate

fun List<StreakUi>.currentStreakLength(today: LocalDate): Int {
    return maxByOrNull { it.endDate.value }
        ?.takeIf { streak ->
            val end = streak.endDate.value
            end == today || end == today.minusDays(1)
        }?.length ?: 0
}

fun List<StreakUi>.longestStreakLength(): Int {
    return maxByOrNull { it.length }?.length ?: 0
}