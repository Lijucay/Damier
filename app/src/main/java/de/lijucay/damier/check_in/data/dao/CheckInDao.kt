package de.lijucay.damier.check_in.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CheckInDao {
    @Query("SELECT * FROM CheckInInfo WHERE activityId = :activityId")
    fun observeCheckIns(activityId: UUID): Flow<List<CheckInInfo>>

    @Query("SELECT * FROM CheckInInfo WHERE activityId = :activityId")
    suspend fun getCheckIns(activityId: UUID): List<CheckInInfo>

    @Query("SELECT * FROM CheckInInfo")
    suspend fun getAllCheckIns(): List<CheckInInfo>

    @Upsert
    suspend fun upsert(checkInInfo: CheckInInfo)

    @Upsert
    suspend fun upsertAll(checkIns: List<CheckInInfo>)

    @Delete
    suspend fun deleteCheckIn(checkInInfo: CheckInInfo): Int
}
