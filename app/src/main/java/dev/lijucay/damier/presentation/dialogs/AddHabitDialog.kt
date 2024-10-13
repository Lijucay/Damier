package dev.lijucay.damier.presentation.dialogs

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.lijucay.damier.R
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.presentation.composables.HideableInfoCard
import dev.lijucay.damier.presentation.composables.LargeTitleText
import dev.lijucay.damier.util.DataStore.DEFAULT_GOAL
import dev.lijucay.damier.util.DataStore.SHOW_UNIT_INFO_CARD
import dev.lijucay.damier.util.DataStore.dataStore
import dev.lijucay.damier.util.Specs.topPadding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * Justice is merely the construct of the current power base, a base which, according to my
 * calculations, is about to change.
 * [...]
 * Too late for what?! The republic to fall? It already has and you just can't see. There is no
 * justice, no law, no order, except for the one that will replace it. The time of the Jedi has
 * passed.
 * */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(
    sheetState: SheetState,
    onSavePressed: (Habit) -> Unit,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(keyboardHeight) {
        scrollState.scrollBy(keyboardHeight.toFloat())
    }

    var habitTitle by remember { mutableStateOf("") }
    var singularUnitName by remember { mutableStateOf("") }
    var pluralUnitName by remember { mutableStateOf("") }

    var saveButtonEnabled by remember { mutableStateOf(false) }
    var showErrorTitle by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val showUnitInfoCard by context.dataStore.data
        .map { preferences -> preferences[SHOW_UNIT_INFO_CARD] ?: true }
        .collectAsState(false)

    val defaultGoal by context.dataStore.data
        .map { preferences -> preferences[DEFAULT_GOAL] ?: 10 }
        .collectAsState(10)

    var goal by remember { mutableIntStateOf(defaultGoal) }
    var goalString by remember { mutableStateOf(goal.toString()) }

    LaunchedEffect(goal) { goalString = goal.toString() }
    LaunchedEffect(goalString) {
        goalString.toIntOrNull()?.let {
            goal = it
        }
    }
    LaunchedEffect(defaultGoal) { goal = defaultGoal }

    LaunchedEffect(habitTitle) {
        saveButtonEnabled = habitTitle.isNotBlank()
        showErrorTitle = habitTitle.isBlank()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        val uiSystemController = rememberSystemUiController()
        uiSystemController.setNavigationBarColor(Color.Transparent)
        uiSystemController.isNavigationBarContrastEnforced = false

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding())
                .verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }

                LargeTitleText(text = stringResource(R.string.add_habit))

                IconButton(
                    onClick = {
                        onSavePressed(
                            Habit(
                                title = habitTitle,
                                singularUnitName = singularUnitName.ifBlank {
                                    context.getString(R.string.default_singular_unit)
                                },
                                pluralUnitName = pluralUnitName.ifBlank {
                                    context.getString(R.string.default_plural_unit)
                                },
                                goal = if (
                                    goalString.toIntOrNull() == null ||
                                    goalString.toInt() == 0
                                ) defaultGoal else goalString.toInt()
                            )
                        )
                    },
                    enabled = saveButtonEnabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = { Text(text = stringResource(R.string.habit_title)) },
                value = habitTitle,
                onValueChange = { newTitle ->
                    habitTitle = newTitle
                },
                isError = showErrorTitle,
                supportingText = {
                    if (showErrorTitle) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.title_cannot_be_empty),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
            LargeTitleText(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
                text = stringResource(R.string.unit)
            )

            HideableInfoCard(
                title = stringResource(R.string.unit_info_title),
                message = stringResource(R.string.unit_explanation),
                visible = showUnitInfoCard
            ) {
                scope.launch {
                    context.dataStore.edit { preferences ->
                        preferences[SHOW_UNIT_INFO_CARD] = !showUnitInfoCard
                    }
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = { Text(text = stringResource(R.string.singular_unit_name)) },
                value = singularUnitName,
                onValueChange = { newSingularUnit ->
                    singularUnitName = newSingularUnit
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                label = { Text(text = stringResource(R.string.plural_unit_name)) },
                value = pluralUnitName,
                onValueChange = { newPluralUnit ->
                    pluralUnitName = newPluralUnit
                }
            )

            LargeTitleText(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
                text = stringResource(R.string.goal_title),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { goal-- },
                    enabled = goal > 1
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = stringResource(R.string.remove)
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.width(100.dp),
                    value = goalString,
                    onValueChange = { newValue -> goalString = newValue }
                )

                IconButton(
                    onClick = { goal++ }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    }
}