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
    private val _trackingInfoCollectionJobs = mutableMapOf<String, Job>()
    private val _waffleBoardInfoCollectionJobs = mutableMapOf<String, Job>()

    private val _trackingInfoMap = MutableStateFlow(mutableMapOf<String, List<TrackingInfo>>())
    val trackingInfoMap: StateFlow<Map<String, List<TrackingInfo>>> = _trackingInfoMap

    private val _waffleBoardInfoMap = MutableStateFlow(mutableMapOf<String, List<DateCount>>())
    val waffleBoardInfoMap: StateFlow<Map<String, List<DateCount>>> = _waffleBoardInfoMap

    fun collectTrackingInfo(habits: List<Habit>) {
        Log.d("TrackingViewModel", "Collecting tracking info with habits: $habits")

        _trackingInfoCollectionJobs.values.forEach { it.cancel() }
        _trackingInfoCollectionJobs.clear()

        val habitTitles = habits.map { it.title }.toSet()

        _trackingInfoMap.update { it.filterKeys { key -> key in habitTitles }.toMutableMap() }
        Log.d("TrackingViewModel", "tracking info map was updated: ${trackingInfoMap.value}")

        habits.forEach { habit ->
            val title = habit.title
            val job = viewModelScope.launch {
                repository.getTrackingInfo(title)?.collect { info ->
                    _trackingInfoMap.update { map ->
                        map.toMutableMap().apply { this[title] = info }
                    }
                }
            }

            _trackingInfoCollectionJobs[title] = job
        }
    }

    fun collectWaffleBoardInfo(habits: List<Habit>) {
        Log.d("TrackingViewModel", "Collecting waffle board info with habits: $habits")

        _waffleBoardInfoCollectionJobs.values.forEach { it.cancel() }
        _waffleBoardInfoCollectionJobs.clear()

        val habitTitles = habits.map { it.title }.toSet()

        _waffleBoardInfoMap.update { it.filterKeys { key -> key in habitTitles }.toMutableMap() }
        Log.d("TrackingViewModel", "Waffle-Board map was updated: ${waffleBoardInfoMap.value}")

        habits.forEach { habit ->
            val title = habit.title
            val job = viewModelScope.launch {
                repository.getWaffleDiagramData(title).collect { data ->
                    _waffleBoardInfoMap.update { map ->
                        map.toMutableMap().apply { this[title] = data }
                    }
                }
            }

            _waffleBoardInfoCollectionJobs[title] = job
        }
    }

    fun insertTrackingInfo(trackingInfo: TrackingInfo) = viewModelScope.launch {
        repository.insertTrackingInfo(trackingInfo)
    }

    fun removeTrackingInfo(habitTitle: String, trackingInfo: TrackingInfo) {
        viewModelScope.launch {
            repository.deleteTrackingInfo(trackingInfo)
        }.invokeOnCompletion {
            _trackingInfoMap.update { map ->
                val list = map[habitTitle]

                list?.let {
                    val newList = it.toMutableList()
                    newList.remove(trackingInfo)

                    map.toMutableMap().apply { replace(habitTitle, newList.toList()) }
                } ?: map
            }
        }
    }
}