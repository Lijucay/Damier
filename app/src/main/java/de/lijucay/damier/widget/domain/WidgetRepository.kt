package de.lijucay.damier.widget.domain

import de.lijucay.damier.activity.data.model.Activity
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WidgetRepository {
    suspend fun getActivityById(id: UUID): Activity?
    suspend fun getAllActivities(): List<ActivityInfo>
    fun observeActivity(id: UUID): Flow<WidgetActivityState>
    suspend fun getDefaultAmount(activityId: UUID): Int?
}