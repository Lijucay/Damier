package de.lijucay.damier.core.presentation.models

import java.util.UUID

data class StreakUi(
    val id: UUID,
    val activityId: UUID,
    val startDate: DisplayableDate,
    val endDate: DisplayableDate,
    val length: Int
)