package de.lijucay.damier.core.domain

import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ActivityRepository {
    fun getActivities(): Flow<List<Activity>>
    fun observeActivity(id: UUID): Flow<Activity?>
    suspend fun getActivityById(id: UUID): ActivityInfo?
    suspend fun upsertActivity(activity: ActivityInfo)
    suspend fun deleteActivity(activity: ActivityInfo)
    
    suspend fun upsertCheckIn(checkIn: CheckInInfo)
    suspend fun deleteCheckIn(checkIn: CheckInInfo)
}
