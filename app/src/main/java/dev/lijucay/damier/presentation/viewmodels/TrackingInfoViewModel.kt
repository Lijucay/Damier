package dev.lijucay.damier.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lijucay.damier.data.local.model.DateCount
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.data.local.model.TrackingInfo
import dev.lijucay.damier.data.local.repository.TrackingInfoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingInfoViewModel @Inject constructor(
    private val repository: TrackingInfoRepository
) : ViewModel() {
    private val _trackingInfoCollectionJobs = mutableMapOf<Int, Job>()
    private val _waffleBoardInfoCollectionJobs = mutableMapOf<Int, Job>()

    private val _trackingInfoMap = MutableStateFlow(mutableMapOf<Int, List<TrackingInfo>>())
    val trackingInfoMap: StateFlow<Map<Int, List<TrackingInfo>>> = _trackingInfoMap

    private val _waffleBoardInfoMap = MutableStateFlow(mutableMapOf<Int, List<DateCount>>())
    val waffleBoardInfoMap: StateFlow<Map<Int, List<DateCount>>> = _waffleBoardInfoMap

    fun collectTrackingInfo(habits: List<Habit>) {
        Log.d("TrackingViewModel", "Collecting tracking info with habits: $habits")

        _trackingInfoCollectionJobs.values.forEach { it.cancel() }
        _trackingInfoCollectionJobs.clear()

        val habitIDs = habits.map { it.id }.toSet()

        _trackingInfoMap.update { it.filterKeys { key -> key in habitIDs }.toMutableMap() }
        Log.d("TrackingViewModel", "tracking info map was updated: ${trackingInfoMap.value}")

        habits.forEach { habit ->
            val id = habit.id
            val job = viewModelScope.launch {
                repository.getTrackingInfo(id)?.collect { info ->
                    _trackingInfoMap.update { map ->
                        map.toMutableMap().apply { this[id] = info }
                    }
                }
            }

            _trackingInfoCollectionJobs[id] = job
        }
    }

    fun collectWaffleBoardInfo(habits: List<Habit>) {
        Log.d("TrackingViewModel", "Collecting waffle board info with habits: $habits")

        _waffleBoardInfoCollectionJobs.values.forEach { it.cancel() }
        _waffleBoardInfoCollectionJobs.clear()

        val habitIDs = habits.map { it.id }.toSet()

        _waffleBoardInfoMap.update { it.filterKeys { key -> key in habitIDs }.toMutableMap() }
        Log.d("TrackingViewModel", "Waffle-Board map was updated: ${waffleBoardInfoMap.value}")

        habits.forEach { habit ->
            val id = habit.id
            val job = viewModelScope.launch {
                repository.getWaffleDiagramData(id).collect { data ->
                    _waffleBoardInfoMap.update { map ->
                        map.toMutableMap().apply { this[id] = data }
                    }
                }
            }

            _waffleBoardInfoCollectionJobs[id] = job
        }
    }

    fun insertTrackingInfo(trackingInfo: TrackingInfo) = viewModelScope.launch {
        repository.insertTrackingInfo(trackingInfo)
    }

    fun removeTrackingInfo(habitID: Int, trackingInfo: TrackingInfo) {
        viewModelScope.launch {
            repository.deleteTrackingInfo(trackingInfo.id)
        }.invokeOnCompletion {
            _trackingInfoMap.update { map ->
                val list = map[habitID]

                list?.let {
                    val newList = it.toMutableList()
                    newList.remove(trackingInfo)

                    map.toMutableMap().apply { replace(habitID, newList.toList()) }
                } ?: map
            }
        }
    }
}