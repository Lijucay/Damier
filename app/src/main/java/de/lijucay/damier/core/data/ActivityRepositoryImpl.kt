package de.lijucay.damier.core.data

import androidx.room.withTransaction
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.NfcChipDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.NfcChipInfo
import de.lijucay.damier.core.data.wrapper.toActivityDomain
import de.lijucay.damier.core.data.wrapper.toActivityDomains
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.CheckInFactory
import de.lijucay.damier.core.domain.NfcCheckInResult
import de.lijucay.damier.core.domain.StreakDataSource
import de.lijucay.damier.core.domain.models.ActivityDomain
import de.lijucay.damier.shared.ReferenceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID

class ActivityRepositoryImpl(
    private val database: DamierDatabase,
    private val activityDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val nfcChipDao: NfcChipDao,
    private val streakDataSource: StreakDataSource,
) : ActivityRepository {
    override fun getActivities(): Flow<List<ActivityDomain>> =
        activityDao.getActivities().map { it.toActivityDomains() }

    override suspend fun getActivitiesWithCheckIns(): List<ActivityDomain> =
        activityDao.getActivitiesWithCheckIns().toActivityDomains()

    override fun observeActivity(id: UUID): Flow<ActivityDomain?> =
        activityDao.observeSelectedActivity(id).map { it?.toActivityDomain() }

    override suspend fun getActivityById(id: UUID): ActivityInfo? =
        activityDao.getActivityInfoById(id)

    override suspend fun upsertActivity(activity: ActivityInfo) {
        activityDao.upsert(activity)
        recalculateStreakForActivity(activity.id)
    }

    override suspend fun deleteActivity(activity: ActivityInfo) {
        activityDao.deleteActivity(activity)
    }

    override suspend fun upsertCheckIn(checkIn: CheckInInfo) {
        database.withTransaction {
            checkInDao.upsert(checkIn)
            recalculateStreakForActivity(checkIn.activityId)
        }
    }

    override suspend fun deleteCheckIn(checkIn: CheckInInfo) {
        database.withTransaction {
            checkInDao.deleteCheckIn(checkIn)
            recalculateStreakForActivity(checkIn.activityId)
        }
    }

    override suspend fun linkNfcChip(chipId: UUID, activityId: UUID) {
        nfcChipDao.insert(
            NfcChipInfo(chipId = chipId, activityId = activityId, linkedAt = LocalDateTime.now())
        )
    }

    override suspend fun unlinkNfcChip(chipId: UUID): UUID? {
        val chip = nfcChipDao.getByChipId(chipId) ?: return null
        nfcChipDao.deleteByChipId(chipId)
        return chip.activityId
    }

    override suspend fun updateNfcChipLabel(label: String, chipId: UUID) {
        nfcChipDao.updateLabel(label, chipId)
    }

    override suspend fun checkInByNfcChipId(chipId: UUID): NfcCheckInResult? {
        val activityId = nfcChipDao.getActivityIdForChip(chipId) ?: return null
        val activity = activityDao.getActivityById(activityId)?.activityInfo ?: return null

        val checkIn = CheckInFactory.createQuickCheckIn(activity.id, activity.defaultAmount)
        upsertCheckIn(checkIn)

        return NfcCheckInResult(activity.id, activity.activityName)
    }

    private suspend fun recalculateStreakForActivity(activityId: UUID) {
        val activity = activityDao.getActivityInfoById(activityId) ?: return
        streakDataSource.recalculateStreak(
            activityId = activityId,
            reference = if (activity.referenceType == ReferenceType.GOAL) activity.reference else 1
        )
    }
}
