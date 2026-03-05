package de.lijucay.damier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import de.lijucay.damier.activity_details.presentation.ActivityDetailsScreen
import de.lijucay.damier.activity_list.ActivityListScreen
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.activity_list.AddActivityItemScreen
import de.lijucay.damier.core.presentation.DetailsDestination
import de.lijucay.damier.core.presentation.UIViewModel
import de.lijucay.damier.core.presentation.components.AdaptivePane
import de.lijucay.damier.core.presentation.exampleActivities
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val windowAdaptiveInfo = currentWindowAdaptiveInfo()
            val isWidthAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            )
            val isHeightAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
            )

            val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
                adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies(
                    detailPaneAdaptStrategy = if (isWidthAtLeastExpanded)
                        AdaptStrategy.Reflow(
                            reflowUnder = ListDetailPaneScaffoldRole.List
                        ) else AdaptStrategy.Hide
                )
            )

            val scope = rememberCoroutineScope()
            val activityListViewModel = koinViewModel<ActivityListViewModel>()
            val uiViewModel = koinViewModel<UIViewModel>()

            val detailsPage by uiViewModel.detailsPage.collectAsStateWithLifecycle()

            DamierTheme {
                AdaptivePane(
                    listPane = {
                        ActivityListScreen(
                            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
                            activityList = exampleActivities(),
                            onActivityClicked = { activityUi ->
                                activityListViewModel.observeSelectedActivity(activityUi.id)

                                uiViewModel.setDetailsPage(DetailsDestination.ActivityDetails(activityUi))
                                scope.launch {
                                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            },
                            onAddActivity = {
                                uiViewModel.setDetailsPage(DetailsDestination.AddActivity)
                                scope.launch {
                                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            }
                        )
                    },
                    detailPane = {
                        detailsPage?.let { detailsDestination ->
                            when (detailsDestination) {
                                is DetailsDestination.ActivityDetails -> {
                                    ActivityDetailsScreen(
                                        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
                                        isHeightAtLeastExpanded = isHeightAtLeastExpanded,
                                        activityListViewModel = activityListViewModel
                                    ) { scope.launch { scaffoldNavigator.navigateBack() } }
                                }
                                DetailsDestination.AddActivity -> {
                                    AddActivityItemScreen(
                                        isWidthAtLeastExpanded = isWidthAtLeastExpanded                                    )
                                }
                            }
                        } ?: Box(modifier = Modifier.fillMaxSize()) {
                            Text(text = stringResource(R.string.no_activity_selected))
                        }
                    },
                    showDetailPane = isWidthAtLeastExpanded,
                    scaffoldNavigator = scaffoldNavigator
                )
            }
        }
    }
}