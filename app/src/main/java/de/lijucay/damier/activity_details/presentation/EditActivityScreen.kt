package de.lijucay.damier.activity_details.presentation

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.domain.ActivityFormMode
import de.lijucay.damier.core.presentation.screens.ActivityFormScreen
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
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