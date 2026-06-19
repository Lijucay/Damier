package de.lijucay.damier.core.data

import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.domain.StreakDataSource
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
        val checkIns = checkInDao.getCheckIns(activityId)

        val newStreaks = buildStreaks(
            activityId = activityId,
            checkIns = checkIns,
            reference = reference,
            today = LocalDate.now()
        )

        streakDao.replaceAllForActivity(activityId, newStreaks)
    }

    companion object {
        fun buildStreaks(
            activityId: UUID,
            checkIns: List<CheckInInfo>,
            reference: Int,
            today: LocalDate
        ): List<Streak> {
            if (checkIns.isEmpty()) return emptyList()

            val amountByDate: Map<LocalDate, Int> = checkIns
                .groupBy { it.timestamp.toLocalDate() }
                .mapValues { (_, entries) -> entries.sumOf { it.amount } }

            val firstDate = amountByDate.keys.min()

            val newStreaks = mutableListOf<Streak>()
            var streakStart: LocalDate? = null
            var streakEnd: LocalDate? = null
            var streakLength = 0
            var cursor = firstDate

            while (!cursor.isAfter(today)) {
                val reached = amountByDate[cursor] ?: 0

                if (reached >= reference) {
                    if (streakStart == null) streakStart = cursor
                    streakEnd = cursor
                    streakLength++
                } else {
                    if (streakStart != null && streakEnd != null) {
                        newStreaks.add(
                            Streak(
                                activityId = activityId,
                                startDate = streakStart,
                                endDate = streakEnd,
                                length = streakLength
                            )
                        )
                    }

                    streakStart = null
                    streakEnd = null
                    streakLength = 0
                }

                cursor = cursor.plusDays(1)
            }

            if (streakStart != null && streakEnd != null) {
                newStreaks.add(
                    Streak(
                        activityId = activityId,
                        startDate = streakStart,
                        endDate = streakEnd,
                        length = streakLength
                    )
                )
            }

            return newStreaks
        }
    }
}