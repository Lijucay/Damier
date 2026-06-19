package de.lijucay.damier.activity_details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.presentation.LongUnitName
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.components.Stepper
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import de.lijucay.damier.core.presentation.models.toDisplayableTime
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.design.components.TitleText
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInForm(
    sheetState: SheetState,
    mode: CheckInFormMode,
    unit: LongUnitName,
    useLimitTheme: Boolean,
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
        initialText = if (state.amount == 1) "" else state.amount.toString()
    )

    val dateState = rememberDatePickerState(
        initialSelectedDate = initialDateTime.toLocalDate(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val date = Date().apply {
                    time = utcTimeMillis
                }
                val currentDate = Date()
                return date.before(currentDate)
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
                                    imageVector = Icons.Rounded.Delete,
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
                                imageVector = Icons.Rounded.Save,
                                contentDescription = stringResource(R.string.save)
                            )
                        }
                    }
                }
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                            Box(
                                Modifier
                                    .clip(shape = MaterialTheme.shapes.extraLarge)
                                    .background(
                                        if (useLimitTheme)
                                            MaterialTheme.colorScheme.onErrorContainer
                                        else
                                            MaterialTheme.colorScheme.tertiaryContainer
                                    )
                            ) {
                                DefaultText(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = if (useLimitTheme)
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.onTertiaryContainer,
                                    text = dateState.getSelectedDate()!!.toDisplayableDate().formatted
                                )
                            }
                        }

                        AnimatedVisibility(visible = state.showDatePicker) {
                            DatePicker(
                                colors = DatePickerDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    weekdayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    yearContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    currentYearContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    disabledYearContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                                    selectedYearContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedYearContentColor = MaterialTheme.colorScheme.primaryContainer,
                                    dayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedDayContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedDayContentColor = MaterialTheme.colorScheme.primaryContainer,
                                    disabledDayContentColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(0.5f),
                                    todayDateBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    todayContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    subheadContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    dividerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    navigationContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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

                        Box(
                            Modifier
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .background(
                                    if (useLimitTheme)
                                        MaterialTheme.colorScheme.onErrorContainer
                                    else
                                        MaterialTheme.colorScheme.tertiaryContainer
                                )
                        ) {
                            DefaultText(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = if (useLimitTheme)
                                    MaterialTheme.colorScheme.errorContainer
                                else
                                    MaterialTheme.colorScheme.onTertiaryContainer,
                                text = LocalTime.of(timeState.hour, timeState.minute)
                                    .toDisplayableTime().formatted
                            )
                        }
                    }

                    AnimatedVisibility(visible = state.showTimePicker) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            TimePicker(
                                colors = TimePickerDefaults.colors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                                    timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    periodSelectorBorderColor = MaterialTheme.colorScheme.primaryContainer,
                                    clockDialSelectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                                    clockDialUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    periodSelectorSelectedContentColor = MaterialTheme.colorScheme.primaryContainer,
                                    clockDialColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    periodSelectorUnselectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                                state = timeState
                            )
                        }
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
