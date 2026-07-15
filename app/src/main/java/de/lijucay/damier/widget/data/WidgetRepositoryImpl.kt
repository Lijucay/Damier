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
    override suspend fun getActivityById(id: UUID): Activity? =
        activityDao.getActivityById(id)

    override suspend fun getAllActivities(): List<ActivityInfo> =
        activityDao.getActivitiesForWidgetConfig()

    override fun observeActivity(id: UUID): Flow<WidgetActivityState> =
        activityDao.observeWidgetActivityData(id).map { data ->
            if (data != null) WidgetActivityState.Loaded(data)
            else WidgetActivityState.Deleted
        }

    override suspend fun getDefaultAmount(activityId: UUID): Int? =
        activityDao.getDefaultAmount(activityId)
}