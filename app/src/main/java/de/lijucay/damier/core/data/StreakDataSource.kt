package de.lijucay.damier.core.data

import de.lijucay.damier.core.presentation.models.CheckInUi
import java.time.LocalDate
import java.util.UUID

interface StreakDataSource {
    suspend fun recalculateStreak(
        activityId: UUID,
        reference: Int = 1
    )
}