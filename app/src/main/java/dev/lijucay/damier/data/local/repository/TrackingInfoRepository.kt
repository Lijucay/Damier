package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.TrackingInfo
import kotlinx.coroutines.flow.Flow

interface TrackingInfoRepository {
    fun getTrackingInfo(habitId: Int): Flow<List<TrackingInfo>>?
    suspend fun insertTrackingInfo(trackingInfo: TrackingInfo): Long
    fun getWaffleDiagramData(habitId: Int): Flow<List<DateCount>>
    suspend fun deleteTrackingInfo(id: Int): Int
}