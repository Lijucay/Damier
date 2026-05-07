package de.lijucay.damier

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.layout.AdaptStrategy
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldDefaults
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.core.layout.WindowSizeClass
import de.lijucay.cue_read.CueReadManager
import de.lijucay.cue_read.ReadResult
import de.lijucay.damier.activity_details.presentation.ActivityDetailsScreen
import de.lijucay.damier.activity_details.presentation.ActivityDetailsViewModel
import de.lijucay.damier.activity_details.presentation.EditActivityScreen
import de.lijucay.damier.activity_list.presentation.ActivityListScreen
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.activity_list.presentation.AddActivityItemScreen
import de.lijucay.damier.core.domain.DeletionMode
import de.lijucay.damier.core.presentation.DetailsDestination
import de.lijucay.damier.core.presentation.components.AdaptivePane
import de.lijucay.damier.core.presentation.dialogs.DeletionDialog
import de.lijucay.damier.core.presentation.dialogs.InfoDialog
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.settings.presentation.SettingsScreen
import de.lijucay.damier.ui.theme.DamierTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class MainActivity : ComponentActivity() {
    private val detailsViewModel: ActivityDetailsViewModel by viewModel()
    private val listViewModel: ActivityListViewModel by viewModel()

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        handleNfcIntent(intent)

        val activityId = intent.getStringExtra("activityId")?.let { id ->
            UUID.fromString(id)
        }

        setContent {
            val uiViewModel = koinViewModel<UIViewModel>()
            val activityListViewModel = koinViewModel<ActivityListViewModel>()
            koinViewModel<ActivityDetailsViewModel>()

            val windowAdaptiveInfo = currentWindowAdaptiveInfoV2()
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

            LaunchedEffect(activityId) {
                activityId?.let { id ->
                    activityListViewModel.observeSelectedActivity(id)
                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }

            val detailsPage by uiViewModel.detailsPage.collectAsStateWithLifecycle()
            val deletionMode by uiViewModel.deletionDialogMode.collectAsStateWithLifecycle()
            val infoMode by uiViewModel.infoMode.collectAsStateWithLifecycle()

            val snackbarHostState = remember { SnackbarHostState() }

            val showSnackbar: (String, Boolean, String?, (() -> Unit)?) -> Unit = remember {
                { message, showButton, buttonText, action ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = if (showButton) buttonText else null,
                            duration = SnackbarDuration.Long
                        )

                        if (result == SnackbarResult.ActionPerformed) action?.invoke()
                    }
                }
            }

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
                                    ActivityDetailsScreen(
                                        onEditActivity = {
                                            uiViewModel.setDetailsPage(DetailsDestination.EditActivity)
                                            scope.launch { scaffoldNavigator.navigateTo(
                                                pane = ListDetailPaneScaffoldRole.Detail
                                            ) }
                                        }
                                    ) {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                is DetailsDestination.AddActivity -> {
                                    AddActivityItemScreen {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                is DetailsDestination.Settings -> {
                                    SettingsScreen(
                                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                                        showCrashlyticsChangedSnackbar = { snackbarSumId, show, buttonTxtId, action ->
                                            showSnackbar(
                                                this@MainActivity.getString(snackbarSumId),
                                                show,
                                                this@MainActivity.getString(buttonTxtId),
                                                action
                                            )
                                        }
                                    ) {
                                        scope.launch { scaffoldNavigator.navigateBack() }
                                    }
                                }
                                is DetailsDestination.EditActivity -> {
                                    EditActivityScreen {
                                        scope.launch {
                                            scaffoldNavigator.navigateBack()
                                        }
                                        uiViewModel.setDetailsPage(DetailsDestination.ActivityDetails)
                                    }
                                }
                            }
                        }
                    },
                    showDetailPane = isWidthAtLeastExpanded,
                    scaffoldNavigator = scaffoldNavigator
                )

                deletionMode?.let { mode ->
                    DeletionDialog(
                        mode = mode,
                        onDismissRequest = { uiViewModel.setDeletionMode(null) },
                        onDelete = {
                            when (mode) {
                                is DeletionMode.Activity -> {
                                    uiViewModel.setDeletionMode(null)
                                    activityListViewModel.deleteActivity(mode.activity)
                                    scope.launch { scaffoldNavigator.navigateBack() }
                                }
                                is DeletionMode.CheckIn -> {
                                    uiViewModel.setDeletionMode(null)
                                    activityListViewModel.deleteCheckIn(mode.checkIn)
                                }
                            }
                        }
                    )
                }

                infoMode?.let {
                    InfoDialog(it) { uiViewModel.setInfoMode(null) }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleNfcIntent(intent)
    }

    private fun handleNfcIntent(intent: Intent) {
        when (intent.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED -> {
                if (detailsViewModel.state.value.nfcWriteState is NfcWriteState.WaitingForTag) {
                    val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                    val activityId = listViewModel.selectedActivity.value?.id ?: return
                    detailsViewModel.onTagDiscovered(tag, activityId)
                    return
                }

                if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
                    lifecycleScope.launch { handleNfcRead(intent) }
                }
            }
        }
    }

    private suspend fun handleNfcRead(intent: Intent) {
        val result = withContext(Dispatchers.IO) { CueReadManager().read(intent) }

        if (result !is ReadResult.Success) return
        listViewModel.checkInByNfcChipId(result.chipId)
    }
}