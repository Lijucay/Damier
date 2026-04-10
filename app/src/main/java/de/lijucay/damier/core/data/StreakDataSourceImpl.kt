package de.lijucay.damier.core.data

import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.StreakDataSource
import de.lijucay.damier.core.presentation.getCurrentStreak
import de.lijucay.damier.core.presentation.models.toCheckInUi
import java.time.LocalDate
import java.util.UUID

class StreakDataSourceImpl(
    private val streakDao: StreakDao,
    private val checkInDao: CheckInDao
): StreakDataSource {
//    override suspend fun recalculateStreak(activityId: UUID, reference: Int) {
//        val checkIns = checkInDao.getCheckIns(activityId)
//            .groupBy { it.timestamp.toLocalDate() }
//            .mapValues { entry -> entry.value.map { it.toCheckInUi() } }
//
//        val dates = checkIns.keys.sorted()
//
//        val newStreaks = mutableListOf<Streak>()
//        var streakStart: LocalDate? = null
//        var streakEnd: LocalDate? = null
//        var streakLength = 0
//
//        var cursor = dates.first()
//        val today = LocalDate.now()
//
//        while (!cursor.isAfter(today)) {
//            val reached = checkIns[cursor]?.sumOf { it.amount } ?: 0
//
//            if (reached >= reference) {
//                if (streakStart == null) streakStart = cursor
//                streakEnd = cursor
//                streakLength++
//            } else {
//                if (streakStart != null && streakEnd != null) {
//                    newStreaks.add(
//                        Streak(
//                            activityId = activityId,
//                            startDate = streakStart,
//                            endDate = streakEnd,
//                            length = streakLength
//                        )
//                    )
//                }
//
//                streakStart = null
//                streakEnd = null
//                streakLength = 0
//            }
//
//            cursor = cursor.plusDays(1)
//        }
//
//        if (streakStart != null && streakEnd != null) {
//            newStreaks.add(
//                Streak(
//                    activityId = activityId,
//                    startDate = streakStart,
//                    endDate = streakEnd,
//                    length = streakLength
//                )
//            )
//        }
//
//        streakDao.replaceAllForActivity(activityId, newStreaks)
//    }

    override suspend fun recalculateStreak(
        activityId: UUID,
        reference: Int
    ) {
        val latest = streakDao.getLatest(activityId)
        val today = LocalDate.now()

        val checkIns = checkInDao.getCheckIns(activityId)
            .groupBy { it.timestamp.toLocalDate() }
            .mapValues { checkIn -> checkIn.value.map { it.toCheckInUi() } }

        val currentLength = checkIns.getCurrentStreak(
            today,
            reference
        )

        when {
            currentLength == 0 -> {
                if (latest != null) streakDao.delete(latest)
            }
            latest != null && latest.endDate >= today.minusDays(1) -> {
                streakDao.upsert(
                    latest.copy(
                        startDate = today.minusDays(currentLength.toLong() - 1),
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