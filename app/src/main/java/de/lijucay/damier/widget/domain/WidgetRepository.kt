package de.lijucay.damier.widget.domain

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface WidgetRepository {
    suspend fun getActivityById(id: UUID): Activity?
    suspend fun getAllActivities(): List<ActivityInfo>
    fun observeActivity(id: UUID): Flow<Activity?>
}