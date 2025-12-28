package de.lijucay.damier.activity_list

import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.R
import de.lijucay.damier.core.data.daos.ActivityInfoDao
import de.lijucay.damier.core.data.wrapper.toActivityUis
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.presentation.exampleActivities
import de.lijucay.damier.core.presentation.models.ActivityUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ActivityListViewModel(
    private val activityDao: ActivityInfoDao
) : ViewModel() {
    private val _activities = MutableStateFlow(emptyList<ActivityUi>())
    val activities = _activities
        .onStart { loadActivities() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _selectedActivity = MutableStateFlow<ActivityUi?>(null)
    val selectedActivity = _selectedActivity.asStateFlow()

    private fun loadActivities() {
        viewModelScope.launch {
            activityDao.getActivities()
                .collect { activityInfos ->
                    _activities.value = activityInfos.toActivityUis()
                }
        }
    }

    fun observeSelectedActivity(id: UUID) {
        _selectedActivity.value = ActivityUi(
            id = UUID.randomUUID(),
            title = "Reading",
            unitId = UnitId.PAGES,
            reference = 10,
            referenceType = ReferenceType.GOAL
        )

    //        viewModelScope.launch {
//
//        }
    }
}