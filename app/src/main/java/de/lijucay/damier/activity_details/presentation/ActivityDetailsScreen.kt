package de.lijucay.damier.activity_details.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.clipWithScreenSize
import de.lijucay.damier.core.presentation.components.WaffleDiagram
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.ChartCheckIn
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar
import java.lang.Math.random
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActivityDetailsScreen(
    modifier: Modifier = Modifier,
    isWidthAtLeastExpanded: Boolean,
    isHeightAtLeastExpanded: Boolean,
    activityListViewModel: ActivityListViewModel,
    onNavigateBack: () -> Unit,
) {
    val selectedActivity by activityListViewModel.selectedActivity.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = selectedActivity?.title ?: "")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                navigationIcon = {
                    if (!isWidthAtLeastExpanded)
                        IconButton(
                            onClick = onNavigateBack
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = null
                            )
                        }
                }
            )
        },
        bottomBar = {
            if (isHeightAtLeastExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        modifier = Modifier
                            .paddingWithSafeNavigationBar(16.dp)
                            .size(80.dp),
                        onClick = {
                        },
                        shape = MaterialShapes.Cookie12Sided.toShape(),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Bolt,
                            contentDescription = stringResource(R.string.check_in)
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets
            .displayCutout
            .exclude(WindowInsets.safeContent)
            .add(WindowInsets.statusBars),
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clipWithScreenSize(isWidthAtLeastExpanded, isHeightAtLeastExpanded)
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize()
        ) {
            AnimatedContent(targetState = selectedActivity) { activity ->
                if (activity == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(R.string.no_activity_selected))
                    }
                } else {
                    ActivityDetails(activityUi = activity)
                }
            }
        }
    }
}

@Composable
private fun ActivityDetails(modifier: Modifier = Modifier, activityUi: ActivityUi) {
    val containerColor = if (activityUi.referenceType != ReferenceType.LIMIT)
        MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.errorContainer

    LazyColumn(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 4.dp
                ),
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
                        checkIns = listOf()
                    )
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 2.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 4.dp,
                    bottomStart = 24.dp,
                    bottomEnd = 24.dp
                )
            ) {
                WeeklyChart(
                    currentWeekCheckIns = DayOfWeek.entries.map {
                        val short = it.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault())

                        ChartCheckIn(short, Random.nextInt(0, 100))
                    }
                )
            }
        }
    }
}