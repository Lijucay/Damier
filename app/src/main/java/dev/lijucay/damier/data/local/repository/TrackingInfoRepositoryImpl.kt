package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.database.TrackingInfoDao
import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class TrackingInfoRepositoryImpl @Inject constructor(
    private val trackingInfoDao: TrackingInfoDao
) : TrackingInfoRepository {
    override fun getTrackingInfo(habitTitle: String): Flow<List<TrackingInfo>>? {
        return trackingInfoDao.getTrackingInfo(habitTitle)
    }

    override suspend fun insertTrackingInfo(trackingInfo: TrackingInfo): Long {
        return trackingInfoDao.insertTrackingInfo(trackingInfo)
    }

    override fun getWaffleDiagramData(habitTitle: String): Flow<List<DateCount>> {
        val endDate = LocalDate.now()
        val startDate = endDate.minusWeeks(13).with(DayOfWeek.MONDAY)

        val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        return trackingInfoDao.getCountsForDateRange(
            habitTitle = habitTitle,
            startDate = startDate.format(formatter),
            endDate = endDate.format(formatter)
        ).map { list ->
            list.mapNotNull { item ->
                if (item.date != null)
                    DateCount(
                        date = LocalDate.parse(item.date),
                        totalCount = item.totalCount ?: 0
                    )
                else null
            }
        }
    }

    override suspend fun deleteTrackingInfo(trackingInfo: TrackingInfo): Int {
        return trackingInfoDao.deleteTrackingInfo(trackingInfo)
    }
}