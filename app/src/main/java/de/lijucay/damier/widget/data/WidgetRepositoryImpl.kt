package de.lijucay.damier.widget.data

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.widget.domain.WidgetActivityState
import de.lijucay.damier.widget.domain.WidgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class WidgetRepositoryImpl(private val activityDao: ActivityInfoDao) : WidgetRepository {
    override suspend fun getActivityById(id: UUID): Activity? {
        return activityDao.getActivityById(id)
    }

    override suspend fun getAllActivities(): List<ActivityInfo> {
        return activityDao.getActivitiesForWidgetConfig()
    }

    override fun observeActivity(id: UUID): Flow<WidgetActivityState> {
        return activityDao.observeSelectedActivity(id).map { activity ->
            if (activity != null) WidgetActivityState.Loaded(activity)
            else WidgetActivityState.Deleted
        }
    }
}