package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.flow.Flow

interface TrackingInfoRepository {
    fun getTrackingInfo(habitTitle: String): Flow<List<TrackingInfo>>?
    suspend fun insertTrackingInfo(trackingInfo: TrackingInfo): Long
    fun getWaffleDiagramData(habitTitle: String): Flow<List<DateCount>>
    suspend fun deleteTrackingInfo(trackingInfo: TrackingInfo): Int
}