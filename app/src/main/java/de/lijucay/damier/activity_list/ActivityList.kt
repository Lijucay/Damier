package de.lijucay.damier.activity_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.UIViewModel
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.ui.shared.activity_list.ActivityListItem
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.util.UUID

@Composable
fun ActivityList(
    modifier: Modifier = Modifier,
    onActivityClicked: (UUID) -> Unit
) {
    val uiViewModel = koinViewModel<UIViewModel>()
    val activityListViewModel = koinViewModel<ActivityListViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val activities by activityListViewModel.activities.collectAsStateWithLifecycle()

    val showReference by uiViewModel.showReference.collectAsStateWithLifecycle()
    val showMaxAmount by uiViewModel.showMaxAmount.collectAsStateWithLifecycle()

    LazyColumn(
        contentPadding = PaddingValues(bottom = bottomPadding() + 70.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            activities,
            key = { it.id }
        ) { activityUi ->
            ActivityListItem(
                modifier = Modifier.animateItem(fadeInSpec = motionScheme.defaultSpatialSpec()),
                activityUi = activityUi,
                onCheckInClicked = {
                    val checkIn = CheckInInfo(
                        activityId = activityUi.id,
                        timestamp = LocalDateTime.now(),
                        checkInCount = activityUi.defaultAmount
                    )

                    activityListViewModel.insertCheckIn(checkIn)
                },
                showReference = showReference,
                showMaxAmount = showMaxAmount,
            ) {
                activityListViewModel.observeSelectedActivity(activityUi.id)
                onActivityClicked(activityUi.id)
            }
        }
    }
}