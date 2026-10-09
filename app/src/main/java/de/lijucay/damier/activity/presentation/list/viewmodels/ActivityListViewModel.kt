package de.lijucay.damier.activity.presentation.list.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import de.lijucay.damier.check_in.data.model.dto.CheckInInfo
import de.lijucay.damier.activity.data.wrapper.toActivityInfo
import de.lijucay.damier.check_in.data.wrapper.toCheckInInfo
import de.lijucay.damier.activity.domain.ActivityRepository
import de.lijucay.damier.backup.domain.exports.ExportResult
import de.lijucay.damier.backup.domain.exports.ExportUtil
import de.lijucay.damier.backup.domain.imports.ImportUtil
import de.lijucay.damier.activity.presentation.ActivityUi
import de.lijucay.damier.check_in.presentation.CheckInUi
import de.lijucay.damier.activity.presentation.toActivityDomain
import de.lijucay.damier.activity.presentation.toActivityUi
import de.lijucay.damier.activity.presentation.toActivityUis
import de.lijucay.damier.check_in.presentation.toCheckInDomain
import de.lijucay.damier.notifications.NotificationHelper
import de.lijucay.damier.widget.presentation.DamierWidgetState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ActivityListViewModel(
    private val repository: ActivityRepository,
    private val importUtil: ImportUtil,
    private val exportUtil: ExportUtil,
    private val widgetState: DamierWidgetState,
    private val notificationHelper: NotificationHelper,
) : ViewModel() {
    val activities = repository.getActivities()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = listOf()
        ).map { it.toActivityUis() }

    private val _selectedActivityId = MutableStateFlow<UUID?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val selectedActivity: StateFlow<ActivityUi?> = _selectedActivityId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else repository.observeActivity(id).map { it?.toActivityUi() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun observeSelectedActivity(id: UUID) {
        _selectedActivityId.value = id
    }

    fun upsert(activity: ActivityInfo) {
        viewModelScope.launch {
            repository.upsertActivity(activity)
            val time = activity.reminderTime
            if (activity.remindersEnabled && time != null)
                notificationHelper.scheduleNotification(activity.id, activity.activityName, time)
        }
    }

    fun upsert(checkIn: CheckInInfo) {
        viewModelScope.launch {
            repository.upsertCheckIn(checkIn)
            widgetState.updateWidgetForActivity(checkIn.activityId)
        }
    }

    fun deleteActivity(activityUi: ActivityUi) {
        val activity = activityUi.toActivityDomain().toActivityInfo()

        viewModelScope.launch {
            repository.deleteActivity(activity)
            widgetState.updateWidgetForActivity(activityUi.id)
        }
    }

    fun deleteCheckIn(checkInUi: CheckInUi) {
        val checkIn = checkInUi.toCheckInDomain().toCheckInInfo()

        viewModelScope.launch {
            runCatching { repository.deleteCheckIn(checkIn) }
                .onSuccess { widgetState.updateWidgetForActivity(checkInUi.activityId) }
        }
    }

    fun import(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit,
        onIncompatibleVersion: () -> Unit
    ) {
        viewModelScope.launch {
            importUtil.importData(
                fileUri,
                onTotalCountUpdate,
                onCurrentCountUpdate,
                onComplete,
                onIncompatibleVersion
            )
        }
    }

    fun exportData(
        dirUri: Uri,
        onComplete: (ExportResult) -> Unit
    ) {
        viewModelScope.launch {
            onComplete(exportUtil.exportData(dirUri))
        }
    }
}