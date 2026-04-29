package de.lijucay.damier.widget.data

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.widget.domain.WidgetRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class WidgetRepositoryImpl(private val activityDao: ActivityInfoDao) : WidgetRepository {
    override suspend fun getActivityById(id: UUID): Activity? {
        return activityDao.getActivityById(id)
    }

    override suspend fun getAllActivities(): List<ActivityInfo> {
        return activityDao.getActivitiesForWidgetConfig()
    }

    override fun observeActivity(id: UUID): Flow<Activity?> {
        return activityDao.observeSelectedActivity(id)
    }
}