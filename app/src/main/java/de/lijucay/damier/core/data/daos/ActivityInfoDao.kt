package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Query
import de.lijucay.damier.core.data.entities.ActivityInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityInfoDao {
    @Query("SELECT * FROM ActivityInfo")
    fun getActivities(): Flow<List<ActivityInfo>>
}