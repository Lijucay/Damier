package de.lijucay.damier.core.data

import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.presentation.getCurrentStreak
import de.lijucay.damier.core.presentation.models.toCheckInUi
import java.time.LocalDate
import java.util.UUID

class StreakDataSourceImpl(
    private val streakDao: StreakDao,
    private val checkInDao: CheckInDao
): StreakDataSource {
    override suspend fun recalculateStreak(
        activityId: UUID,
        reference: Int
    ) {
        val latest = streakDao.getLatest(activityId)
        val today = LocalDate.now()

        val checkIns = checkInDao.getCheckIns(activityId)
            .groupBy { it.timestamp.toLocalDate() }
            .mapValues { it.value.map { it.toCheckInUi() } }

        val currentLength = checkIns.getCurrentStreak(
            today,
            reference
        )

        when {
            currentLength == 0 -> return
            latest != null && latest.endDate >= today.minusDays(1) -> {
                streakDao.upsert(
                    latest.copy(
                        endDate = today,
                        length = currentLength
                    )
                )
            }
            else -> {
                streakDao.upsert(
                    Streak(
                        activityId = activityId,
                        startDate = today.minusDays(currentLength.toLong() - 1),
                        endDate = today,
                        length = currentLength
                    )
                )
            }
        }
    }
}