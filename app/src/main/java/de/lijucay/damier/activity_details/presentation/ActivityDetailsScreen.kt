package de.lijucay.damier.activity_details.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.ui.shared.activity_details.components.CheckInItem
import de.lijucay.damier.ui.shared.activity_details.components.StreakCard
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.core.data.entities.CheckInInfo
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.getShortUnitNamesById
import de.lijucay.damier.core.presentation.UIViewModel
import de.lijucay.damier.ui.shared.core.ScreenContainer
import de.lijucay.damier.ui.shared.core.WaffleDiagram
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar
import de.lijucay.damier.ui.shared.core.CookieButton
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActivityDetailsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val uiViewModel = koinViewModel<UIViewModel>()
    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val isHeightAtLeastExpanded by uiViewModel.isHeightAtLeastExpanded.collectAsStateWithLifecycle()

    val selectedActivity by activityListViewModel.selectedActivity.collectAsStateWithLifecycle()
    val title = remember(selectedActivity?.id) { selectedActivity?.title }

    val shouldUseLimitTheme = selectedActivity?.referenceType == ReferenceType.LIMIT

    ScreenContainer(
        modifier = modifier,
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = title,
        bottomBarContent = {
            CookieButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (shouldUseLimitTheme)
                        colorScheme.error
                    else
                        colorScheme.tertiary,
                    contentColor = if (shouldUseLimitTheme)
                        colorScheme.onError
                    else
                        colorScheme.onTertiary
                )
            ) {
                /*Todo: Show check in dialog*/
                selectedActivity?.let {
                    activityListViewModel.insertCheckIn(
                        CheckInInfo(
                            activityId = it.id,
                            timestamp = LocalDateTime.now(),
                            checkInCount = it.defaultAmount
                        )
                    )
                }
            }
        },
        showBottomBarContent = isHeightAtLeastExpanded,
        navigationIcon = {
            if (!isWidthAtLeastExpanded) IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    ) {
        AnimatedContent(
            targetState = selectedActivity,
            contentKey = { it?.id },
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith
                        fadeOut(animationSpec = tween(300))
            }
        ) { activity ->
            if (activity != null) {
                ActivityDetails(activityUi = activity)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.no_activity_selected))
                }
            }
        }
    }
}

@Composable
private fun ActivityDetails(
    modifier: Modifier = Modifier,
    activityUi: ActivityUi
) {
    val containerColor = if (activityUi.referenceType != ReferenceType.LIMIT)
        colorScheme.primaryContainer
    else colorScheme.errorContainer

    val context = LocalContext.current
    val today = LocalDate.now()

    val todaysCheckIns = activityUi.groupedCheckIns[today] ?: emptyList()
    val currentStreak = activityUi.streaks.maxByOrNull { it.endDate.value }
    val longestStreak = activityUi.streaks.maxByOrNull { it.length }

    val unitNames = activityUi.unitId.getShortUnitNamesById(context = context)
    val shouldUseLimitTheme = activityUi.referenceType == ReferenceType.LIMIT

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            Column {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = containerColor,
                        contentColor = contentColorFor(containerColor)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        WaffleDiagram(
                            reference = activityUi.reference,
                            type = activityUi.referenceType,
                            checkIns = activityUi.groupedCheckIns.values.flatten()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        item {
            Column {
                StreakCard(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    currentStreak = currentStreak?.length ?: 0,
                    longestStreak = longestStreak?.length ?: 0
                )

                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.today),
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Box(
                    Modifier
                        .clip(shape = shapes.extraLarge)
                        .background(
                            if (shouldUseLimitTheme)
                                colorScheme.errorContainer
                            else
                                colorScheme.tertiaryContainer
                        )
                ) {
                    val todayCheckInAmount =
                        activityUi.groupedCheckIns[today]?.sumOf { it.checkInCount } ?: 0

                    val unitName = if (todayCheckInAmount == 1)
                        unitNames.shortUnitSingular
                    else
                        unitNames.shortUnitPlural
                    val displayText = "$todayCheckInAmount $unitName"

                    AnimatedContent(
                        targetState = displayText,
                        transitionSpec = {
                            (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                        }
                    ) { text ->
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = if (shouldUseLimitTheme)
                                colorScheme.onErrorContainer
                            else
                                colorScheme.onTertiaryContainer,
                            text = text
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(todaysCheckIns) { checkIn ->
            CheckInItem(
                checkInUi = checkIn,
                isItemFirst = todaysCheckIns.first() == checkIn,
                isItemLast = todaysCheckIns.last() == checkIn,
                unitName = if (checkIn.checkInCount > 1)
                    unitNames.shortUnitPlural
                else
                    unitNames.shortUnitSingular,
                usesLimitColors = activityUi.referenceType == ReferenceType.LIMIT
            )
        }
    }
}