package dev.lijucay.damier.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.DateCountNullable
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackingInfoDao {
    @Query("select * from TrackingInfo where habitId = :habitId order by date desc")
    fun getTrackingInfo(habitId: Int): Flow<List<TrackingInfo>>?

    @Insert(TrackingInfo::class, OnConflictStrategy.IGNORE)
    suspend fun insertTrackingInfo(trackingInfo: TrackingInfo): Long

    @Query(
        """
            select date, sum(count) as totalCount
            from TrackingInfo
            where habitId = :habitId and date between :startDate and :endDate
            group by date
        """
    )
    fun getCountsForDateRange(habitId: Int, startDate: String, endDate: String): Flow<List<DateCountNullable>>

    @Query("delete from TrackingInfo where id = :id")
    suspend fun deleteTrackingInfo(id: Int): Int
}