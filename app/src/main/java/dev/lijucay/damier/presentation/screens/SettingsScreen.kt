package dev.lijucay.damier.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FlagCircle
import androidx.compose.material.icons.rounded.LocalPolice
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.edit
import dev.lijucay.damier.R
import dev.lijucay.damier.presentation.composables.preferences.Preference
import dev.lijucay.damier.presentation.composables.preferences.PreferenceCategoryTitle
import dev.lijucay.damier.presentation.composables.preferences.SwitchPreference
import dev.lijucay.damier.presentation.viewmodels.UIViewModel
import dev.lijucay.damier.util.DataStore.DEFAULT_GOAL
import dev.lijucay.damier.util.DataStore.SHOW_GOAL
import dev.lijucay.damier.util.DataStore.dataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    uiViewModel: UIViewModel,
    onOpenLicenseScreen: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showGoal by context.dataStore.data.map { preferences ->
        preferences[SHOW_GOAL] ?: false
    }.collectAsState(false)

    val defaultGoal by context.dataStore.data.map { preferences ->
        preferences[DEFAULT_GOAL] ?: 10
    }.collectAsState(10)

    LaunchedEffect(Unit) {
        uiViewModel.setCurrentTitle(context.getString(R.string.settings))
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        PreferenceCategoryTitle(stringResource(R.string.damier_settings))
        SwitchPreference(
            checked = showGoal,
            title = stringResource(R.string.show_goal),
            summary = stringResource(
                if (showGoal) R.string.show_goal_true
                else R.string.show_goal_false
            ),
            iconVector = Icons.Rounded.FlagCircle
        ) {
            scope.launch {
                context.dataStore.edit { preferences ->
                    preferences[SHOW_GOAL] = it
                }
            }
        }
        Preference(
            title = stringResource(R.string.default_goal),
            summary = stringResource(R.string.current_default_goal, defaultGoal),
            iconVector = Icons.Rounded.SportsScore
        ) { uiViewModel.setShowEditDefaultGoalDialog(true) }

        PreferenceCategoryTitle(stringResource(R.string.info))
        Preference(
            title = stringResource(R.string.licenses),
            summary = stringResource(R.string.licenses_summary),
            iconVector = Icons.Rounded.LocalPolice,
            onClick = onOpenLicenseScreen
        )
    }
}