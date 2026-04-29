package de.lijucay.damier.activity_list.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.widget.presentation.DamierWidget
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

    val activities by activityListViewModel.activities.collectAsStateWithLifecycle()

    val showReference by uiViewModel.showReference.collectAsStateWithLifecycle()
    val showMaxAmount by uiViewModel.showMaxAmount.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = activities.isEmpty()
    ) { it ->
        if (it) {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.SelfImprovement,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.no_activities),
                    style = typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = modifier,
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
                                amount = activityUi.defaultAmount
                            )

                            activityListViewModel.upsert(checkIn)
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
    }
}
