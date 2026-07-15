package de.lijucay.damier

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowSizeClass
import de.lijucay.damier.core.presentation.Navigator
import de.lijucay.damier.core.presentation.adaptiveHorizontalCutoutPadding
import de.lijucay.damier.core.presentation.dialogs.InfoDialog
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.debug.DebugDataSeeder
import de.lijucay.damier.nfc.NfcManager
import de.lijucay.damier.onboarding.OnBoarding
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityRetainedScope
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.scope.Scope
import java.util.UUID

class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityRetainedScope()

    private val nfcManager: NfcManager by lazy { scope.get() }
    private val navigator: Navigator by lazy { scope.get() }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class, KoinExperimentalAPI::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleActivityIdIntent(intent)

        lifecycleScope.launch {
            nfcManager.handleNfcIntent(intent)

            if (BuildConfig.DEBUG) {
                lifecycleScope.launch {
                    DebugDataSeeder(get()).seed()
                }
            }
        }

        setContent {
            val uiViewModel = koinViewModel<UIViewModel>(scope = scope)
            val coroutineScope = rememberCoroutineScope()
            val snackbarHostState = remember { scope.get<SnackbarHostState>() }

            val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
            val isWidthAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            )
            val isHeightAtLeastExpanded = windowAdaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
                WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND
            )

            LaunchedEffect(isWidthAtLeastExpanded, isHeightAtLeastExpanded) {
                uiViewModel.setWindowSizeInfo(isWidthAtLeastExpanded, isHeightAtLeastExpanded)
            }

            val firstLaunch by uiViewModel.firstLaunch.collectAsStateWithLifecycle()
            val listDetailsStrategy = rememberListDetailSceneStrategy<Any>(
                paneExpansionState = rememberPaneExpansionState(),
                paneExpansionDragHandle = { state ->
                    val interactionSource = remember { MutableInteractionSource() }
                    VerticalDragHandle(
                        modifier = Modifier.paneExpansionDraggable(
                            state,
                            LocalMinimumInteractiveComponentSize.current,
                            interactionSource
                        ),
                        interactionSource = interactionSource
                    )
                }
            )
            val infoMode by uiViewModel.infoMode.collectAsStateWithLifecycle()
            val shouldShowSnackbar by uiViewModel.showSnackbar.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                uiViewModel.snackbarEvent.collect { event ->
                    if (shouldShowSnackbar) {
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = event.message,
                                actionLabel = if (event.showButton) event.buttonText else null,
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                event.action?.invoke()
                            }
                        }
                    }
                }
            }

            DamierTheme {
                val intAnimationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<IntOffset>()
                val floatAnimationSpec = MaterialTheme.motionScheme.defaultSpatialSpec<Float>()

                AnimatedContent(
                    firstLaunch,
                    transitionSpec = {
                        (slideInVertically(intAnimationSpec) { it / 16 } + fadeIn(floatAnimationSpec))
                            .togetherWith(slideOutVertically(intAnimationSpec) { it / 16 } + fadeOut(floatAnimationSpec))
                    }
                ) {
                    if (it) {
                        OnBoarding(uiViewModel = uiViewModel) {
                            uiViewModel.setFirstLaunch(false)
                        }
                    } else {
                        val entryProvider = koinEntryProvider<Any>(scope)

                        NavDisplay(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .adaptiveHorizontalCutoutPadding(
                                    start = if(isWidthAtLeastExpanded) 15.dp else 0.dp,
                                    end = if (isWidthAtLeastExpanded) 15.dp else 0.dp,
                                    bottom = if (isWidthAtLeastExpanded) 15.dp else 0.dp
                                ),
                            backStack = navigator.backStack,
                            sceneStrategies = listOf(listDetailsStrategy),
                            onBack = { navigator.goBack() },
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator()
                            ),
                            transitionSpec = {
                                slideInHorizontally(intAnimationSpec) { it } togetherWith
                                        slideOutHorizontally(intAnimationSpec) { -it }
                            },
                            popTransitionSpec = {
                                slideInHorizontally(intAnimationSpec) { -it } togetherWith
                                        slideOutHorizontally(intAnimationSpec) { it }
                            },
                            predictivePopTransitionSpec = {
                                slideInHorizontally(intAnimationSpec) { -it } togetherWith
                                        slideOutHorizontally(intAnimationSpec) { it }
                            },
                            entryProvider = entryProvider
                        )
                    }
                }

                infoMode?.let {
                    InfoDialog(it) { uiViewModel.setInfoMode(null) }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        lifecycleScope.launch {
            nfcManager.handleNfcIntent(intent)
        }

        handleActivityIdIntent(intent)
    }

    private fun handleActivityIdIntent(intent: Intent?) {
        intent?.getStringExtra("activityId")
            ?.let { runCatching { UUID.fromString(it) }.getOrNull() }
            ?.let { navigator.goToActivityDetailsFresh(it) }
    }
}
