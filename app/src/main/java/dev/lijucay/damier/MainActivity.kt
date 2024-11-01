package dev.lijucay.damier

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.lijucay.damier.data.local.model.TrackingInfo
import dev.lijucay.damier.presentation.dialogs.AddHabitDialog
import dev.lijucay.damier.presentation.dialogs.CheckIns
import dev.lijucay.damier.presentation.dialogs.EditHabitDialog
import dev.lijucay.damier.presentation.dialogs.ImportDialog
import dev.lijucay.damier.presentation.dialogs.IncrementCountDialog
import dev.lijucay.damier.presentation.dialogs.InfoDialog
import dev.lijucay.damier.presentation.screens.MainScreen
import dev.lijucay.damier.presentation.viewmodels.HabitViewModel
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.presentation.viewmodels.UIViewModel
import dev.lijucay.damier.ui.theme.DamierTheme
import dev.lijucay.damier.util.DataStore.DEFAULT_GOAL
import dev.lijucay.damier.util.DataStore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val habitViewModel: HabitViewModel by viewModels()
    private val trackingInfoViewModel: TrackingInfoViewModel by viewModels()
    private val uiViewModel: UIViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        habitViewModel.loadHabits()

        setContent {
            DamierTheme {
                val scope = rememberCoroutineScope()
                val context = LocalContext.current
                val habitList by habitViewModel.habitList.collectAsStateWithLifecycle()
                val showImportDialog by uiViewModel.showImportDialog.collectAsStateWithLifecycle()

                trackingInfoViewModel.collectTrackingInfo(habitList)
                trackingInfoViewModel.collectWaffleBoardInfo(habitList)
                val defaultGoal by context.dataStore.data
                    .map { preferences -> preferences[DEFAULT_GOAL] ?: 10 }
                    .collectAsStateWithLifecycle(10)

                MainScreen(uiViewModel, habitViewModel, trackingInfoViewModel)

                val currentHabitTitle by habitViewModel.currentSelectedHabit.collectAsStateWithLifecycle()

                val showCounterDialog by uiViewModel.showCounterDialog.collectAsStateWithLifecycle()
                val showAddHabitDialog by uiViewModel.showAddHabitDialog.collectAsStateWithLifecycle()

                val showInfoDialog by uiViewModel.showInfoDialog.collectAsStateWithLifecycle()
                val showEditHabitDialog by uiViewModel.showEditHabitDialog.collectAsStateWithLifecycle()
                val showEditDefaultGoalDialog by uiViewModel.showEditDefaultGoalDialog.collectAsStateWithLifecycle()
                val showCheckIns by uiViewModel.showCheckIns.collectAsStateWithLifecycle()

                val currentFileUri by uiViewModel.currentFileUri.collectAsStateWithLifecycle()

                if (showCheckIns) {
                    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                    currentHabitTitle?.let { title ->
                        val currentHabit = habitList.find { it.title == title }

                        currentHabit?.let {
                            CheckIns(
                                sheetState = sheetState,
                                habit = it,
                                trackingInfoViewModel = trackingInfoViewModel
                            ) {
                                hideSheet(scope, sheetState) {
                                    uiViewModel.setShowCheckIns(false)
                                }
                            }
                        }
                    }
                }

                if (showCounterDialog) {
                    val sheetState = rememberModalBottomSheetState()
                    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                    val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

                    currentHabitTitle?.let { title ->
                        val currentHabit = habitList.find { habit ->
                            habit.title == title
                        }

                        currentHabit?.let {
                            IncrementCountDialog(
                                sheetState = sheetState,
                                onSavePressed = { count ->
                                    hideSheet(scope, sheetState) {
                                        uiViewModel.setShowCounterDialog(false)
                                        trackingInfoViewModel.insertTrackingInfo(
                                            TrackingInfo(
                                                habitTitle = title,
                                                date = LocalDate.now().format(dateFormatter),
                                                time = LocalTime.now().format(timeFormatter),
                                                count = count
                                            )
                                        )
                                    }
                                },
                                onDismissRequest = {
                                    hideSheet(scope, sheetState) {
                                        uiViewModel.setShowCounterDialog(false)
                                    }
                                },
                                habitTitle = currentHabit.title
                            )
                        }
                    }
                }

                if (showAddHabitDialog) {
                    val sheetState = rememberModalBottomSheetState(
                        skipPartiallyExpanded = true
                    )

                    AddHabitDialog(
                        sheetState = sheetState,
                        onSavePressed = { habit ->
                            hideSheet(scope, sheetState) {
                                uiViewModel.setShowAddHabitDialog(false)
                                habitViewModel.insertHabit(habit)
                            }
                        },
                        onDismissRequest = {
                            hideSheet(scope, sheetState) {
                                uiViewModel.setShowAddHabitDialog(false)
                            }
                        }
                    )
                }

                if (showInfoDialog)
                    InfoDialog(uiViewModel) { uiViewModel.setInfoDialogInfo(show = false) }

                if (showEditHabitDialog) {
                    currentHabitTitle?.let { title ->
                        val habit = habitList.find { it.title == title }
                        habit?.let {
                            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                            EditHabitDialog(
                                sheetState = sheetState,
                                habit = it,
                                onSavePressed = { newHabit ->
                                    hideSheet(scope, sheetState) {
                                        habitViewModel.updateHabit(newHabit)
                                        uiViewModel.setShowEditHabitDialog(false)
                                    }
                                },
                                onDismissRequest = {
                                    hideSheet(scope, sheetState) {
                                        uiViewModel.setShowEditHabitDialog(false)
                                    }
                                }
                            )
                        }
                    }
                }

                if (showEditDefaultGoalDialog) {
                    val sheetState = rememberModalBottomSheetState()
                    IncrementCountDialog(
                        sheetState = sheetState,
                        customCount = defaultGoal,
                        habitTitle = stringResource(R.string.default_goal),
                        onSavePressed = { newGoal ->
                            hideSheet(scope, sheetState) {
                                uiViewModel.setShowEditDefaultGoalDialog(false)
                                scope.launch {
                                    context.dataStore.edit { preferences ->
                                        preferences[DEFAULT_GOAL] = newGoal
                                    }
                                }
                            }
                        }
                    ) {
                        hideSheet(scope, sheetState) {
                            uiViewModel.setShowEditDefaultGoalDialog(false)
                        }
                    }
                }

                if (showImportDialog) {
                    currentFileUri?.let {
                        ImportDialog(
                            onDismiss = { },
                            onComplete = {
                                uiViewModel.setShowImportDialog(false)
                                uiViewModel.setCurrentFileUri(null)
                            },
                            uiViewModel = uiViewModel,
                            fileUri = it
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun hideSheet(scope: CoroutineScope, sheetState: SheetState, action: () -> Unit) {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) action()
        }
    }
}