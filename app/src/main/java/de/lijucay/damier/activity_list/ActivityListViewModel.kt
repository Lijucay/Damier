package de.lijucay.damier.activity_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.data.StreakDataSource
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.daos.CheckInDao
import de.lijucay.damier.core.data.daos.StreakDao
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.entities.Streak
import de.lijucay.damier.core.data.wrapper.toActivityUi
import de.lijucay.damier.core.data.wrapper.toActivityUis
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.getCurrentStreak
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

class ActivityListViewModel(
    private val activityDao: ActivityInfoDao,
    private val checkInDao: CheckInDao,
    private val streakDataSource: StreakDataSource
) : ViewModel() {
    private val _activities = MutableStateFlow(emptyList<ActivityUi>())
    val activities = _activities
        .onStart { loadActivities() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _selectedActivityId = MutableStateFlow<UUID?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedActivity: StateFlow<ActivityUi?> = _selectedActivityId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else activityDao.observeSelectedActivity(id)
                .map { it?.toActivityUi() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun loadActivities() {
        viewModelScope.launch {
            activityDao.getActivities()
                .collect { activities ->
                    _activities.value = activities.toActivityUis()
                }
        }
    }

    fun observeSelectedActivity(id: UUID) {
        _selectedActivityId.value = id
    }

    fun clearSelectedActivity() {
        _selectedActivityId.value = null
    }

    fun insertActivity(activity: ActivityInfo) {
        viewModelScope.launch {
            val result = activityDao.insertActivity(activity)
            if (result == -1L) {
                Log.e("ActivityListViewModel", "Could not save activity")
                // TODO: Create "channel" like here (https://github.com/Lijucay/CryptoTracker/blob/initial/app/src/main/java/com/plcoding/cryptotracker/crypto/presentation/coin_list/CoinListViewModel.kt) and show error message
            }
        }
    }

    fun insertCheckIn(checkIn: CheckInInfo) {
        viewModelScope.launch {
            checkInDao.insertCheckIn(checkInInfo = checkIn)

            val activity = activityDao.getActivityById(checkIn.activityId) ?: return@launch

            streakDataSource.recalculateStreak(
                activityId = checkIn.activityId,
                reference = if (activity.referenceType == ReferenceType.GOAL) activity.reference else 1
            )
        }
    }

    fun recalculateStreak(activityId: UUID, reference: Int = 1) {
        Log.e("ActivityListViewModel", "Called recalculateStreak with: activityId = $activityId")

        viewModelScope.launch {
            streakDataSource.recalculateStreak(activityId, reference)
        }
    }
}