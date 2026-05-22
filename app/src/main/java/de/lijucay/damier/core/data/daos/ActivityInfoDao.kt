package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ActivityInfoDao {
    @Query("SELECT * FROM ActivityInfo")
    fun getActivityInfo(): Flow<List<ActivityInfo>>

    @Query("SELECT * FROM ActivityInfo")
    fun getActivityInfoList(): List<ActivityInfo>

    @Upsert
    suspend fun upsert(activity: ActivityInfo)

    @Transaction
    @Query("SELECT * FROM ActivityInfo where id = :id")
    fun observeSelectedActivity(id: UUID): Flow<Activity?>

    @Query("SELECT * FROM ActivityInfo WHERE id = :id LIMIT 1")
    suspend fun getActivityInfoById(id: UUID): ActivityInfo?

    @Query("SELECT * FROM ActivityInfo WHERE id = :id LIMIT 1")
    suspend fun getActivityById(id: UUID): Activity?

    @Query("SELECT * FROM ActivityInfo")
    suspend fun getActivitiesForWidgetConfig(): List<ActivityInfo>

    @Transaction
    @Query("SELECT * FROM ActivityInfo")
    suspend fun getActivitiesWithCheckIns(): List<Activity>

    @Transaction
    @Query("SELECT * FROM ActivityInfo")
    fun getActivities(): Flow<List<Activity>>

    @Delete
    suspend fun deleteActivity(activity: ActivityInfo)

    @Query("UPDATE ActivityInfo SET nfcChipId = :chipId WHERE id = :activityId")
    suspend fun updateNfcChipId(activityId: UUID, chipId: String?)

    @Query("SELECT * FROM ActivityInfo WHERE nfcChipId = :chipId LIMIT 1")
    suspend fun getActivityByNfcChipId(chipId: String): ActivityInfo?
}