package de.lijucay.damier.activity.presentation

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedToggleButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowLeft
import compose.icons.tablericons.Check
import compose.icons.tablericons.X
import de.lijucay.damier.R
import de.lijucay.damier.activity.presentation.list.dialogs.UnitSelectionBottomSheet
import de.lijucay.damier.activity.presentation.list.viewmodels.ActivityListViewModel
import de.lijucay.damier.activity.domain.ActivityUnit
import de.lijucay.damier.core.domain.InfoAction
import de.lijucay.damier.core.domain.InfoMode
import de.lijucay.damier.activity.domain.units
import de.lijucay.damier.core.presentation.components.Stepper
import de.lijucay.damier.core.presentation.components.cards.PreviewCard
import de.lijucay.damier.core.presentation.components.text.LargeTitleText
import de.lijucay.damier.core.presentation.components.text.SmallText
import de.lijucay.damier.core.presentation.components.text.TitleText
import de.lijucay.damier.core.presentation.components.textfields.TitleField
import de.lijucay.damier.core.presentation.dialogs.InfoDialog
import de.lijucay.damier.core.presentation.getLongUnitNamesById
import de.lijucay.damier.core.presentation.layouts.ScreenContainer
import de.lijucay.damier.core.presentation.toStringResource
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.notifications.NotificationHandler
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.shared.UnitGroup
import de.lijucay.damier.shared.UnitId
import de.lijucay.damier.ui.theme.ActivityTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityFormScreen(
    modifier: Modifier = Modifier,
    mode: ActivityFormMode,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val formViewModel = koinViewModel<ActivityFormViewModel>()
    val uiViewModel = koinViewModel<UIViewModel>()

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.PartiallyExpanded, SheetValue.Expanded)
    )
    val scope = rememberCoroutineScope()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val state by formViewModel.state.collectAsStateWithLifecycle()

    val labels = ReferenceType.entries.map { stringResource(it.toStringResource()) }

    val referenceState = rememberTextFieldState(
        initialText = when (mode) {
            is ActivityFormMode.Edit -> mode.activity.reference.toString()
            else -> "1"
        }
    )
    val defaultAmountState = rememberTextFieldState(
        initialText = when (mode) {
            is ActivityFormMode.Edit -> mode.activity.defaultAmount.toString()
            else -> "1"
        }
    )
    val timeState = rememberTimePickerState(
        initialHour = state.reminderTime?.hour ?: 0,
        initialMinute = state.reminderTime?.minute ?: 0
    )

    var hasNotificationPermission = NotificationHandler.checkNotificationPermission(context)
    var isPermanentlyDenied by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        hasNotificationPermission = NotificationHandler.checkNotificationPermission(context)
        onDispose {  }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted)
            formViewModel.setRemindersEnabled(true)
        else
            isPermanentlyDenied = NotificationHandler.isPermissionPermanentlyDenied(context)
    }

    LaunchedEffect(Unit) {
        when (mode) {
            is ActivityFormMode.Add -> formViewModel.initForAdd()
            is ActivityFormMode.Edit -> formViewModel.initForEdit(mode.activity)
        }
    }

    val screenTitle = when (mode) {
        is ActivityFormMode.Add -> stringResource(R.string.add_activity)
        is ActivityFormMode.Edit -> stringResource(R.string.edit, mode.activity.title)
    }

    ActivityTheme(state.useLimitTheme) {
        ScreenContainer(
            modifier = modifier.fillMaxSize(),
            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
            title = screenTitle,
            topAppBarActions = {
                FilledIconButton(
                    enabled = state.isSaveEnabled,
                    onClick = {
                        formViewModel.setReminderTime(LocalTime.of(timeState.hour, timeState.minute))
                        activityListViewModel.upsert(formViewModel.buildActivityInfo())
                        onNavigateBack()
                    }
                ) {
                    Icon(
                        imageVector = TablerIcons.Check,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            },
            navigationIcon = {
                if (!isWidthAtLeastExpanded) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = TablerIcons.ArrowLeft,
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

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

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

                        HorizontalDivider(color = MaterialTheme.colorScheme.primary)

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

                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TitleText(text = stringResource(R.string.reminder))

                        Row(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                                .toggleable(
                                    value = hasNotificationPermission && state.remindersEnabled,
                                    role = Role.Switch,
                                    onValueChange = {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            val activity = context as? androidx.activity.ComponentActivity
                                            when {
                                                NotificationHandler.checkNotificationPermission(context) -> hasNotificationPermission = true
                                                isPermanentlyDenied || (
                                                        activity != null &&
                                                                ActivityCompat.shouldShowRequestPermissionRationale(
                                                                    activity,
                                                                    Manifest.permission.POST_NOTIFICATIONS
                                                                )
                                                        ) -> {
                                                    formViewModel.setShowNotificationInfoDialog(true)
                                                }
                                                else -> launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                            }
                                        }
                                        if (!hasNotificationPermission) {
                                            formViewModel.setShowNotificationInfoDialog(true)
                                        } else {
                                            formViewModel.setRemindersEnabled(!state.remindersEnabled)
                                        }
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                TitleText(text = stringResource(R.string.enable_reminders))
                                Spacer(modifier = Modifier.height(2.dp))
                                SmallText(text = stringResource(R.string.enable_reminders_expl))
                            }

                            Switch(
                                enabled = hasNotificationPermission,
                                modifier = Modifier.padding(end = 16.dp),
                                checked = state.remindersEnabled,
                                onCheckedChange = null,
                                thumbContent = {
                                    Crossfade(
                                        targetState = state.remindersEnabled
                                    ) { isChecked ->
                                        Icon(
                                            imageVector = if (isChecked)
                                                TablerIcons.Check
                                            else
                                                TablerIcons.X,
                                            contentDescription = null,
                                            tint = if (isChecked) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.outlineVariant
                                        )
                                    }
                                }
                            )
                        }
                        AnimatedVisibility(visible = state.remindersEnabled) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                TimePicker(
                                    colors = TimePickerDefaults.colors(
                                        containerColor = MaterialTheme.colorScheme.onPrimary,
                                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                        timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        timeSelectorContainerColor = MaterialTheme.colorScheme.onPrimary,
                                        timeSelectorContentColor = MaterialTheme.colorScheme.primary,
                                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                        periodSelectorBorderColor = MaterialTheme.colorScheme.onPrimary,
                                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary,
                                        clockDialColor = MaterialTheme.colorScheme.onPrimary,
                                        selectorColor = MaterialTheme.colorScheme.primary,
                                    ),
                                    state = timeState
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.showUnitsSelectionDialog) {
            val activityUnit = units.find { it.unitId == state.unitId } ?: ActivityUnit(
                unitId = UnitId.TIMES,
                group = UnitGroup.COUNT
            )

            UnitSelectionBottomSheet(
                sheetState = sheetState,
                selectedUnit = activityUnit,
                onUnitSelected = formViewModel::setUnitId
            ) {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    formViewModel.setShowUnitSelectionSheet(false)
                }
            }
        }

        if (state.showNotificationDialog && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            InfoDialog(
                mode = InfoMode.NotificationPermissionNotGranted,
                onAction = { action ->
                    if (action == InfoAction.GrantNotificationPermission) {
                        formViewModel.setShowNotificationInfoDialog(false)
                        if (!isPermanentlyDenied)
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        else NotificationHandler.openAppNotificationSettings(context)
                    }
                },
                onDismissRequest = { formViewModel.setShowNotificationInfoDialog(false) }
            )
        }
    }
}