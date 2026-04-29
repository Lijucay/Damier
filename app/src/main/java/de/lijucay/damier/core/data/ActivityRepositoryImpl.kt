package de.lijucay.damier.core.data

import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.StreakDataSource
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ActivityRepositoryImpl(
    private val activityDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDataSource: StreakDataSource
) : ActivityRepository {
    override fun getActivities(): Flow<List<Activity>> = activityDao.getActivities()

    override fun observeActivity(id: UUID): Flow<Activity?> = activityDao.observeSelectedActivity(id)

    override suspend fun getActivityById(id: UUID): ActivityInfo? = activityDao.getActivityInfoById(id)

    override suspend fun upsertActivity(activity: ActivityInfo) = activityDao.upsert(activity)

    override suspend fun deleteActivity(activity: ActivityInfo) = activityDao.deleteActivity(activity)

    override suspend fun upsertCheckIn(checkIn: CheckInInfo) {
        checkInDao.upsert(checkIn)
        recalculateStreakForActivity(checkIn.activityId)
    }

    override suspend fun deleteCheckIn(checkIn: CheckInInfo) {
        checkInDao.deleteCheckIn(checkIn)
        recalculateStreakForActivity(checkIn.activityId)
    }

    private suspend fun recalculateStreakForActivity(activityId: UUID) {
        val activity = activityDao.getActivityInfoById(activityId) ?: return
        streakDataSource.recalculateStreak(
            activityId = activityId,
            reference = if (activity.referenceType == ReferenceType.GOAL) activity.reference else 1
        )
    }
}
