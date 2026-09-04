package de.lijucay.damier.activity_details.presentation.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.isInputValid
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.DeviceFloppy
import compose.icons.tablericons.Trash
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.domain.CheckInFormMode
import de.lijucay.damier.activity_details.presentation.viewmodels.CheckInFormViewModel
import de.lijucay.damier.activity_list.presentation.viewmodels.ActivityListViewModel
import de.lijucay.damier.core.presentation.LongUnitName
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.components.AnimatedBadge
import de.lijucay.damier.core.presentation.components.LargeTitleText
import de.lijucay.damier.core.presentation.components.Stepper
import de.lijucay.damier.core.presentation.components.TitleText
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import de.lijucay.damier.core.presentation.models.toDisplayableTime
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInForm(
    sheetState: SheetState,
    mode: CheckInFormMode,
    unit: LongUnitName,
    onDeleteRequest: (CheckInUi) -> Unit,
    onDismissRequest: () -> Unit
) {
    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val formViewModel = koinViewModel<CheckInFormViewModel>()

    val state by formViewModel.state.collectAsStateWithLifecycle()

    val initialDateTime = remember(mode) {
        when (mode) {
            is CheckInFormMode.Edit -> mode.checkIn.dateTime.value
            is CheckInFormMode.Add -> LocalDateTime.now()
        }
    }

    val amountState = rememberTextFieldState(
        initialText = when (mode) {
            is CheckInFormMode.Edit -> mode.checkIn.amount.toString()
            is CheckInFormMode.Add -> mode.defaultAmount.toString()
        }
    )

    val dateState = rememberDatePickerState(
        initialSelectedDate = initialDateTime.toLocalDate(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val selectedDate = Instant.ofEpochMilli(utcTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                val today = LocalDate.now()
                return !selectedDate.isAfter(today)
            }
        }
    )
    val timeState = rememberTimePickerState(
        initialHour = initialDateTime.toLocalTime().hour,
        initialMinute = initialDateTime.toLocalTime().minute
    )

    LaunchedEffect(mode) {
        when (mode) {
            is CheckInFormMode.Add -> formViewModel.initForAdd(mode.activityId)
            is CheckInFormMode.Edit -> formViewModel.initForEdit(mode.checkIn)
        }
    }

    val dialogTitle = when (mode) {
        is CheckInFormMode.Add -> stringResource(R.string.check_in)
        is CheckInFormMode.Edit -> stringResource(R.string.edit_check_in)
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LargeTitleText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = AlertDialogDefaults.titleContentColor,
                        text = dialogTitle
                    )

                    Row {
                        if (mode is CheckInFormMode.Edit) {
                            IconButton(onClick = { onDeleteRequest(mode.checkIn) }) {
                                Icon(
                                    imageVector = TablerIcons.Trash,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }

                        IconButton(
                            enabled = state.isSaveEnabled,
                            onClick = {
                                if (timeState.isInputValid) {
                                    formViewModel.setDateTime(
                                        LocalDateTime.of(
                                            dateState.getSelectedDate(),
                                            LocalTime.of(timeState.hour, timeState.minute)
                                        )
                                    )

                                    activityListViewModel.upsert(formViewModel.buildCheckInInfo())
                                    onDismissRequest()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = TablerIcons.DeviceFloppy,
                                contentDescription = stringResource(R.string.save)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = formViewModel::toggleShowDatePicker
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TitleText(text = stringResource(R.string.date))
                            AnimatedBadge(
                                backgroundColor = MaterialTheme.colorScheme.tertiary,
                                textColor = MaterialTheme.colorScheme.onTertiary,
                                text = dateState.getSelectedDate()!!.toDisplayableDate().formatted
                            )
                        }

                        AnimatedVisibility(visible = state.showDatePicker) {
                            DatePicker(
                                colors = DatePickerDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.onPrimary,
                                    weekdayContentColor = MaterialTheme.colorScheme.primary,
                                    yearContentColor = MaterialTheme.colorScheme.primary,
                                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                                    disabledYearContentColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                                    dayContentColor = MaterialTheme.colorScheme.primary,
                                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledDayContentColor = MaterialTheme.colorScheme.primary.copy(0.5f),
                                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                                    todayContentColor = MaterialTheme.colorScheme.primary,
                                    subheadContentColor = MaterialTheme.colorScheme.primary,
                                    dividerColor = MaterialTheme.colorScheme.primary,
                                    navigationContentColor = MaterialTheme.colorScheme.primary,
                                ),
                                state = dateState,
                                showModeToggle = false,
                                title = null,
                                headline = null
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    onClick = formViewModel::toggleShowTimePicker
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TitleText(text = stringResource(R.string.time))

                        AnimatedBadge(
                            backgroundColor = MaterialTheme.colorScheme.tertiary,
                            textColor = MaterialTheme.colorScheme.onTertiary,
                            text = LocalTime.of(timeState.hour, timeState.minute).toDisplayableTime().formatted
                        )
                    }

                    AnimatedVisibility(visible = state.showTimePicker) {
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

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TitleText(text = stringResource(R.string.note))

                        TextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = state.note ?: "",
                            onValueChange = { newValue -> formViewModel.setNote(newValue) },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                focusedIndicatorColor = Color.Transparent,
                                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                cursorColor = MaterialTheme.colorScheme.onPrimary,
                                selectionColors = TextSelectionColors(
                                    handleColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                                ),
                                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                            ),
                            shape = MaterialTheme.shapes.large,
                            placeholder = {
                                Text(stringResource(R.string.note_placeholder))
                            }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    TitleText(text = stringResource(R.string.amount))

                    Stepper(
                        state = amountState,
                        onValidationChange = { isValid ->
                            if (isValid) formViewModel.setAmount(amountState.text.toString().toIntOrNull() ?: 1)
                        },
                        unit = if ((amountState.text.toString().toIntOrNull() ?: 1) == 1) unit.singularName else unit.pluralName
                    )
                }
            }

            Spacer(Modifier.height(bottomPadding()))
        }
    }
}
