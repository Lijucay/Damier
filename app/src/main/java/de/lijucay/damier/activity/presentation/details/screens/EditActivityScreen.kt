package de.lijucay.damier.activity.presentation.details.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.activity.presentation.ActivityFormMode
import de.lijucay.damier.activity.presentation.ActivityFormScreen
import de.lijucay.damier.activity.presentation.list.viewmodels.ActivityListViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@Composable
fun EditActivityScreen(
    modifier: Modifier = Modifier,
    activityId: String,
    onNavigateBack: () -> Unit,
) {
    val activityListViewModel = koinViewModel<ActivityListViewModel>()

    LaunchedEffect(Unit) {
        activityListViewModel.observeSelectedActivity(UUID.fromString(activityId))
    }

    val selectedActivity by activityListViewModel.selectedActivity.collectAsStateWithLifecycle()

    selectedActivity?.let { activityUi ->
        ActivityFormScreen(
            modifier = modifier,
            mode = ActivityFormMode.Edit(activityUi),
            onNavigateBack = onNavigateBack
        )
    }
}