package dev.lijucay.damier.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.DateCountNullable
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingInfoDao {
    @Query("select * from TrackingInfo where habitTitle = :habitTitle order by date desc")
    fun getTrackingInfo(habitTitle: String): Flow<List<TrackingInfo>>?

    @Query("select * from TrackingInfo")
    fun getCurrentTrackingInfo(): List<TrackingInfo>

    @Insert(TrackingInfo::class, OnConflictStrategy.IGNORE)
    suspend fun insertTrackingInfo(trackingInfo: TrackingInfo): Long

    @Query(
        """
            select date, sum(count) as totalCount
            from TrackingInfo
            where habitTitle = :habitTitle and date between :startDate and :endDate
            group by date
        """
    )
    fun getCountsForDateRange(habitTitle: String, startDate: String, endDate: String): Flow<List<DateCountNullable>>

    @Delete
    suspend fun deleteTrackingInfo(trackingInfo: TrackingInfo): Int
}