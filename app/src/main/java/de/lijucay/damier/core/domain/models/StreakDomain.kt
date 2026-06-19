package de.lijucay.damier.core.domain.models

import java.time.LocalDate
import java.util.UUID

data class StreakDomain(
    val id: UUID,
    val activityId: UUID,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val length: Int
)
