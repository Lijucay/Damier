package de.lijucay.damier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.window.core.layout.WindowSizeClass
import de.lijucay.damier.activity_details.presentation.ActivityDetailsScreen
import de.lijucay.damier.activity_list.ActivityListScreen
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.presentation.components.AdaptivePane
import de.lijucay.damier.core.presentation.exampleActivities
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

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
                    detailPaneAdaptStrategy = if (isWidthAtLeastExpanded) AdaptStrategy.Reflow(
                        reflowUnder = ListDetailPaneScaffoldRole.List
                    ) else AdaptStrategy.Hide
                )
            )

            val scope = rememberCoroutineScope()

            val activityListViewModel = koinViewModel<ActivityListViewModel>()

            DamierTheme {
                AdaptivePane(
                    listPane = {
                        ActivityListScreen(
                            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
                            activityList = exampleActivities()
                        ) { activityUi ->
                            activityListViewModel.observeSelectedActivity(activityUi.id)

                            scope.launch {
                                scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                            }
                        }
                    },
                    detailPane = {
                        ActivityDetailsScreen(
                            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
                            isHeightAtLeastExpanded = isHeightAtLeastExpanded,
                            activityListViewModel = activityListViewModel,
                            onNavigateBack = {
                                if (scaffoldNavigator.canNavigateBack()) {
                                    scope.launch {
                                        scaffoldNavigator.navigateBack()
                                    }
                                }
                            }
                        )
                    },
                    showDetailPane = isWidthAtLeastExpanded,
                    scaffoldNavigator = scaffoldNavigator
                )
            }
        }
    }
}