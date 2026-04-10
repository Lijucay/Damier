package de.lijucay.damier.activity_list.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.wrapper.toActivityInfo
import de.lijucay.damier.core.data.wrapper.toActivityUi
import de.lijucay.damier.core.data.wrapper.toActivityUis
import de.lijucay.damier.core.data.wrapper.toCheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.ExportUtil
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ActivityListViewModel(
    private val repository: ActivityRepository,
    private val importUtil: ImportUtil,
    private val exportUtil: ExportUtil
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
            else repository.observeActivity(id)
                .map { it?.toActivityUi() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private fun loadActivities() {
        viewModelScope.launch {
            repository.getActivities()
                .collect { activities ->
                    _activities.value = activities.toActivityUis()
                }
        }
    }

    fun observeSelectedActivity(id: UUID) {
        _selectedActivityId.value = id
    }

    fun upsert(activity: ActivityInfo) {
        viewModelScope.launch {
            repository.upsertActivity(activity)
        }
    }

    fun upsert(checkIn: CheckInInfo) {
        viewModelScope.launch {
            repository.upsertCheckIn(checkIn)
        }
    }

    fun deleteActivity(activityUi: ActivityUi) {
        val activity = activityUi.toActivityInfo()

        viewModelScope.launch {
            repository.deleteActivity(activity)
        }
    }

    fun deleteCheckIn(checkInUi: CheckInUi) {
        val checkIn = checkInUi.toCheckInInfo()

        viewModelScope.launch {
            repository.deleteCheckIn(checkIn)
        }
    }

    fun import(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            importUtil.importData(
                fileUri,
                onTotalCountUpdate,
                onCurrentCountUpdate,
                onComplete
            )
        }
    }

    fun exportData(
        dirUri: Uri,
        onComplete: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            val result = exportUtil.exportData(dirUri)
            onComplete(result.first, result.second)
        }
    }
}
