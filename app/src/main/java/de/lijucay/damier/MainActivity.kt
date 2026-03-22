package de.lijucay.damier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import de.lijucay.damier.settings.presentation.SettingsScreen
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val activityListViewViewModel = koinViewModel<ActivityListViewModel>()
            val uiViewModel = koinViewModel<UIViewModel>()

            val windowAdaptiveInfo = currentWindowAdaptiveInfo()
            val isWidthAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            )
            val isHeightAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
            )
            val scope = rememberCoroutineScope()

            LaunchedEffect(isWidthAtLeastExpanded, isHeightAtLeastExpanded) {
                uiViewModel.setWindowSizeInfo(isWidthAtLeastExpanded,  isHeightAtLeastExpanded)
            }

            val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
                adaptStrategies = ListDetailPaneScaffoldDefaults.adaptStrategies(
                    detailPaneAdaptStrategy = if (isWidthAtLeastExpanded)
                        AdaptStrategy.Reflow(
                            reflowUnder = ListDetailPaneScaffoldRole.List
                        ) else AdaptStrategy.Hide
                )
            )

            val detailsPage by uiViewModel.detailsPage.collectAsStateWithLifecycle()

            DamierTheme {
                AdaptivePane(
                    listPane = {
                        ActivityListScreen(
                            onActivityClicked = {
                                scope.launch {
                                    uiViewModel.setDetailsPage(DetailsDestination.ActivityDetails)
                                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                }
                            },
                            onSettingsClicked = {
                                scope.launch {
                                    uiViewModel.setDetailsPage(DetailsDestination.Settings)
                                    scaffoldNavigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail
                                    )
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
                        AnimatedContent(targetState = detailsPage) { detailsDestination ->
                            when (detailsDestination) {
                                is DetailsDestination.ActivityDetails -> {
                                    ActivityDetailsScreen {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                DetailsDestination.AddActivity -> {
                                    AddActivityItemScreen {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                is DetailsDestination.Settings -> {
                                    SettingsScreen {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                else -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = stringResource(R.string.no_activity_selected))
                                    }
                                }
                            }
                        }
                    },
                    showDetailPane = isWidthAtLeastExpanded,
                    scaffoldNavigator = scaffoldNavigator
                )
            }
        }
    }
}