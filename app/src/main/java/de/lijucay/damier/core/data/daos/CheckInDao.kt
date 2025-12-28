package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Query
import de.lijucay.damier.core.data.entities.CheckInInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CheckInDao {
    @Query("SELECT * FROM CheckInInfo WHERE activityId = :activityId")
    fun queryCheckIns(activityId: UUID): Flow<List<CheckInInfo>>
}