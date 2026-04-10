package de.lijucay.damier.activity_details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.presentation.components.NumberTextField
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import de.lijucay.damier.core.presentation.models.toDisplayableTime
import de.lijucay.damier.ui.theme.ActivityTheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInForm(
    mode: CheckInFormMode,
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

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

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

    ActivityTheme(useLimitTheme = useLimitTheme) {
        BasicAlertDialog(
            onDismissRequest = onDismissRequest
        ) {
            Surface(
                modifier =
                    Modifier.requiredWidth(360.0.dp)
                        .heightIn(max = 568.0.dp),
                shape = AlertDialogDefaults.shape,
                color = AlertDialogDefaults.containerColor
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .size(AlertDialogDefaults.IconSize),
                            tint = AlertDialogDefaults.iconContentColor,
                            imageVector = when (mode) {
                                is CheckInFormMode.Add -> Icons.Rounded.Bolt
                                is CheckInFormMode.Edit -> Icons.Rounded.Edit
                            },
                            contentDescription = null
                        )
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = AlertDialogDefaults.titleContentColor,
                        style = typography.titleMediumEmphasized,
                        textAlign = TextAlign.Center,
                        text = dialogTitle
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .verticalScroll(rememberScrollState())
                            .then(
                                if (state.showDatePicker || state.showTimePicker) {
                                    Modifier.weight(1f)
                                } else Modifier
                            ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.date))
                            Box(
                                Modifier
                                    .clip(shape = shapes.extraLarge)
                                    .background(
                                        if (useLimitTheme)
                                            colorScheme.error
                                        else
                                            colorScheme.tertiaryContainer
                                    )
                                    .clickable(onClick = formViewModel::toggleShowDatePicker)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = if (useLimitTheme)
                                        colorScheme.onError
                                    else
                                        colorScheme.onTertiaryContainer,
                                    text = dateState.getSelectedDate()!!.toDisplayableDate().formatted
                                )
                            }
                        }

                        AnimatedVisibility(visible = state.showDatePicker) {
                            DatePicker(
                                state = dateState,
                                showModeToggle = false,
                                title = null,
                                headline = null
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = stringResource(R.string.time))

                            Box(
                                Modifier
                                    .clip(shape = shapes.extraLarge)
                                    .background(
                                        if (useLimitTheme)
                                            colorScheme.error
                                        else
                                            colorScheme.tertiaryContainer
                                    )
                                    .clickable(onClick = formViewModel::toggleShowTimePicker)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    color = if (useLimitTheme)
                                        colorScheme.onError
                                    else
                                        colorScheme.onTertiaryContainer,
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
                                    state = timeState
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clickable(
                                    onClick = {
                                        focusManager.clearFocus()
                                        focusRequester.requestFocus()
                                    }
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.amount)
                            )

                            NumberTextField(
                                value = state.amount,
                                onValueChange = formViewModel::setAmount,
                                focusRequester = focusRequester
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(
                                text = stringResource(android.R.string.cancel)
                            )
                        }

                        Row {
                            when(mode) {
                                is CheckInFormMode.Add -> {}
                                is CheckInFormMode.Edit -> {
                                    TextButton(onClick = { onDeleteRequest(mode.checkIn) }) {
                                        Text(
                                            text = stringResource(R.string.delete)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }

                            TextButton(
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
                                Text(text = stringResource(R.string.save))
                            }
                        }
                    }
                }
            }
        }
    }
}