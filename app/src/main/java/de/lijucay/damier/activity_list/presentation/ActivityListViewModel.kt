package de.lijucay.damier.activity_list.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.data.wrapper.toActivityInfo
import de.lijucay.damier.core.data.wrapper.toCheckInInfo
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.ExportResult
import de.lijucay.damier.core.domain.ExportUtil
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toActivityDomain
import de.lijucay.damier.core.presentation.models.toActivityUi
import de.lijucay.damier.core.presentation.models.toActivityUis
import de.lijucay.damier.core.presentation.models.toCheckInDomain
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
