package de.lijucay.damier.core.domain

import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.models.ActivityDomain
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ActivityRepository {
    fun getActivities(): Flow<List<ActivityDomain>>
    suspend fun getActivitiesWithCheckIns(): List<ActivityDomain>
    fun observeActivity(id: UUID): Flow<ActivityDomain?>
    suspend fun getActivityById(id: UUID): ActivityInfo?
    suspend fun upsertActivity(activity: ActivityInfo)
    suspend fun deleteActivity(activity: ActivityInfo)
    
    suspend fun upsertCheckIn(checkIn: CheckInInfo)
    suspend fun deleteCheckIn(checkIn: CheckInInfo)

    suspend fun updateNfcChipId(activityId: UUID, chipId: String?)
    suspend fun getActivityByNfcChipId(chipId: String): ActivityInfo?

    suspend fun checkInByNfcChipId(chipId: String): NfcCheckInResult?
}
