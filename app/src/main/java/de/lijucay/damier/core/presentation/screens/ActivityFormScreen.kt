package de.lijucay.damier.core.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedToggleButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.activity_list.presentation.UnitSelectionBottomSheet
import de.lijucay.damier.core.domain.ActivityFormMode
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.components.Stepper
import de.lijucay.damier.core.presentation.components.TitleField
import de.lijucay.damier.core.presentation.components.cards.PreviewCard
import de.lijucay.damier.core.presentation.components.cards.UnitCard
import de.lijucay.damier.core.presentation.getLongUnitNamesById
import de.lijucay.damier.core.presentation.toStringResource
import de.lijucay.damier.core.presentation.viewmodels.ActivityFormViewModel
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.ui.theme.ActivityTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormScreen(
    modifier: Modifier = Modifier,
    mode: ActivityFormMode,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val uiViewModel = koinViewModel<UIViewModel>()
    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val formViewModel = koinViewModel<ActivityFormViewModel>()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.PartiallyExpanded, SheetValue.Expanded)
    )
    val scope = rememberCoroutineScope()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val state by formViewModel.state.collectAsStateWithLifecycle()

    val labels = ReferenceType.entries.map { stringResource(it.toStringResource()) }

    LaunchedEffect(mode) {
        when (mode) {
            is ActivityFormMode.Add -> formViewModel.initForAdd()
            is ActivityFormMode.Edit -> formViewModel.initForEdit(mode.activity)
        }
    }

    val screenTitle = when (mode) {
        is ActivityFormMode.Add -> stringResource(R.string.add_activity)
        is ActivityFormMode.Edit -> stringResource(R.string.edit, mode.activity.title)
    }

    val referenceState = rememberTextFieldState(
        initialText = if (state.reference == 1) "" else state.reference.toString()
    )

    val defaultAmountState = rememberTextFieldState(
        initialText = if (state.defaultAmount == 1) "" else state.reference.toString()
    )

    ActivityTheme(state.useLimitTheme) {
        ScreenContainer(
            modifier = modifier.fillMaxSize(),
            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
            title = screenTitle,
            topAppBarActions = {
                FilledIconButton(
                    enabled = state.isSaveEnabled,
                    onClick = {
                        activityListViewModel.upsert(formViewModel.buildActivityInfo())
                        onNavigateBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            },
            navigationIcon = {
                if (!isWidthAtLeastExpanded) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                PreviewCard(state = state)

                Spacer(Modifier.height(16.dp))

                LargeTitleText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.title)
                )

                Spacer(Modifier.height(8.dp))

                TitleField(
                    value = state.title,
                    onValueChange = formViewModel::setTitle
                )

                Spacer(Modifier.height(16.dp))

                LargeTitleText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.tracking_options),
                )

                Spacer(Modifier.height(8.dp))

                UnitCard(
                    state = state,
                    onClick = { formViewModel.setShowUnitSelectionSheet(true) }
                )

                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TitleText(text = stringResource(R.string.reference_type))

                        Column{
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
                            ) {
                                val modifiers =
                                    listOf(Modifier.weight(1f), Modifier.weight(1f), Modifier.weight(1f))

                                ReferenceType.entries.forEachIndexed { index, type ->
                                    ElevatedToggleButton(
                                        checked = state.referenceType == type,
                                        onCheckedChange = { formViewModel.setReferenceType(type) },
                                        modifier = modifiers[index].semantics { role = Role.RadioButton },
                                        shapes = when (index) {
                                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                            ReferenceType.entries.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                        }
                                    ) {
                                        Text(labels[index])
                                    }
                                }
                            }

                            AnimatedVisibility(visible = !state.referenceType.isMax()) {
                                Crossfade(targetState = state.referenceType) {
                                    SmallText(
                                        text = stringResource(
                                            id = if (it.isLimit()) {
                                                R.string.limit_desc
                                            } else {
                                                R.string.goal_desc
                                            }
                                        )
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer)

                        TitleText(
                            text = stringResource(
                                id = when (state.referenceType) {
                                    ReferenceType.GOAL -> R.string.target
                                    ReferenceType.LIMIT -> R.string.limit
                                    ReferenceType.MAX -> R.string.max
                                }
                            )
                        )

                        Column {
                            Stepper(
                                state = referenceState,
                                onValidationChange = { isValid ->
                                    if (isValid) formViewModel.setReference(
                                        referenceState.text.toString().toIntOrNull() ?: 1
                                    )
                                },
                                enabled = !state.referenceType.isMax(),
                                unit = if ((referenceState.text.toString().toIntOrNull() ?: 1) == 1) {
                                    state.unitId.getLongUnitNamesById(context).singularName
                                } else {
                                    state.unitId.getLongUnitNamesById(context).pluralName
                                }
                            )

                            AnimatedVisibility(visible = state.referenceType.isMax()) {
                                OutlinedCard(
                                    modifier = Modifier.padding(top = 16.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        modifier = Modifier.padding(16.dp),
                                        text = stringResource(R.string.max_ref_disabled_desc)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer)

                        TitleText(
                            text = stringResource(R.string.default_amount)
                        )

                        Stepper(
                            state = defaultAmountState,
                            onValidationChange = { isValid ->
                                if (isValid) formViewModel.setDefaultAmount(
                                    defaultAmountState.text.toString().toIntOrNull() ?: 1
                                )
                            },
                            unit = if ((defaultAmountState.text.toString().toIntOrNull() ?: 1) == 1) {
                                state.unitId.getLongUnitNamesById(context).singularName
                            } else {
                                state.unitId.getLongUnitNamesById(context).pluralName
                            }
                        )
                    }
                }
            }
        }

        if (state.showUnitsSelectionDialog) {
            UnitSelectionBottomSheet(
                sheetState = sheetState,
                selectedUnit = state.unitId,
                onUnitSelected = formViewModel::setUnitId
            ) {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    formViewModel.setShowUnitSelectionSheet(false)
                }
            }
        }
    }
}