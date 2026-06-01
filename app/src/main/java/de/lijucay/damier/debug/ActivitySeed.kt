package de.lijucay.damier.debug

import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitId
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.random.Random

class DebugDataSeeder(private val repository: ActivityRepository) {

    suspend fun seed() {
        val now = LocalDateTime.now()
        val random = Random(seed = 42)
        val startOfYear = LocalDateTime.of(now.year, 1, 1, 0, 0)
        val daysSoFar = ChronoUnit.DAYS.between(startOfYear, now).toInt()

        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()
        val uuid3 = UUID.randomUUID()

        val activities = listOf(
            ActivityInfo(
                id = uuid1,
                activityName = "Reading",
                unit = UnitId.PAGES,
                reference = 30,
                referenceType = ReferenceType.GOAL,
                defaultAmount = 1
            ),
            ActivityInfo(
                id = uuid2,
                activityName = "Coding",
                unit = UnitId.MINUTES,
                reference = 1,
                referenceType = ReferenceType.MAX,
                defaultAmount = 30
            ),
            ActivityInfo(
                id = uuid3,
                activityName = "Coffee",
                unit = UnitId.CUPS,
                reference = 3,
                referenceType = ReferenceType.LIMIT,
                defaultAmount = 1
            )
        )

        data class ActivitySeed(
            val id: UUID,
            val defaultAmount: Int,
            val skipChance: Float
        )

        val seeds = listOf(
            ActivitySeed(uuid1, 1, 0.3f),
            ActivitySeed(uuid2, 30, 0.4f),
            ActivitySeed(uuid3, 1, 0.2f)
        )

        activities.forEach { repository.upsertActivity(it) }

        seeds.forEach { seed ->
            repeat(daysSoFar) { dayOffset ->
                if (random.nextFloat() > seed.skipChance) {
                    val checkInDate = startOfYear.plusDays(dayOffset.toLong())
                        .withHour(random.nextInt(7, 22))
                        .withMinute(random.nextInt(0, 60))

                    val checkInCount = if (random.nextFloat() > 0.8f) 2 else 1
                    repeat(checkInCount) { i ->
                        repository.upsertCheckIn(
                            CheckInInfo(
                                activityId = seed.id,
                                timestamp = checkInDate.plusHours(i.toLong() * 3),
                                amount = seed.defaultAmount
                            )
                        )
                    }
                }
            }
        }
    }
}