package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import de.lijucay.damier.core.data.Activity
import de.lijucay.damier.core.data.entities.ActivityInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ActivityInfoDao {
    @Query("SELECT * FROM ActivityInfo")
    fun getActivityInfo(): Flow<List<ActivityInfo>>

    @Insert
    suspend fun insertActivity(activity: ActivityInfo): Long

    @Transaction
    @Query("SELECT * FROM ActivityInfo where id = :id")
    fun observeSelectedActivity(id: UUID): Flow<Activity?>

    @Query("SELECT * FROM ActivityInfo WHERE id = :id LIMIT 1")
    suspend fun getActivityById(id: UUID): ActivityInfo?

    @Transaction
    @Query("SELECT * FROM ActivityInfo")
    fun getActivities(): Flow<List<Activity>>
}