package de.lijucay.damier.core.domain

import java.util.UUID

interface StreakDataSource {
    suspend fun recalculateStreak(
        activityId: UUID,
        reference: Int = 1
    )
}