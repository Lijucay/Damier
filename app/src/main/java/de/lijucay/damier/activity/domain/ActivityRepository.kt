package de.lijucay.damier.activity.domain

import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import de.lijucay.damier.nfc.domain.NfcCheckInResult
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

    suspend fun linkNfcChip(chipId: UUID, activityId: UUID)
    suspend fun unlinkNfcChip(chipId: UUID): UUID?
    suspend fun updateNfcChipLabel(label: String, chipId: UUID)

    suspend fun checkInByNfcChipId(chipId: UUID): NfcCheckInResult?
}
