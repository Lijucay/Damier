package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.lijucay.damier.core.data.entities.CheckInInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CheckInDao {
    @Query("SELECT * FROM CheckInInfo WHERE activityId = :activityId")
    fun observeCheckIns(activityId: UUID): Flow<List<CheckInInfo>>

    @Query("SELECT * FROM CheckInInfo WHERE activityId = :activityId")
    suspend fun getCheckIns(activityId: UUID): List<CheckInInfo>

    @Query("SELECT * FROM CheckInInfo")
    fun queryAllCheckIns(): Flow<List<CheckInInfo>>

    @Insert
    suspend fun insertCheckIn(checkInInfo: CheckInInfo): Long

    @Delete
    suspend fun deleteCheckIn(checkInInfo: CheckInInfo): Int
}