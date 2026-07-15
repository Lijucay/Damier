package de.lijucay.damier.settings.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.AlertCircle
import compose.icons.tablericons.CircleCheck
import de.lijucay.damier.R
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.cue.NfcWriteState.Idle.isAutoClosing
import de.lijucay.damier.design.components.LargeText
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NfcResetDialog(
    sheetState: SheetState,
    writeState: NfcWriteState,
    onDismissRequest: () -> Unit
) {
    if (writeState.isAutoClosing) {
        LaunchedEffect(writeState) {
            delay(2000L.milliseconds)
            onDismissRequest()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = writeState
            ) {
                when (it) {
                    is NfcWriteState.EraseException -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(128.dp),
                                imageVector = TablerIcons.AlertCircle,
                                contentDescription = null
                            )

                            LargeText(
                                text = stringResource(R.string.insufficient_size)
                            )
                        }
                    }
                    NfcWriteState.Unknown -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(128.dp),
                                imageVector = TablerIcons.AlertCircle,
                                contentDescription = null
                            )

                            LargeText(
                                text = stringResource(R.string.unknown_error)
                            )
                        }
                    }
                    NfcWriteState.WaitingForTag -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingIndicator(
                                modifier = Modifier
                                    .size(128.dp)
                                    .fillMaxWidth()
                            )

                            LargeText(text = stringResource(R.string.waiting_for_tag))
                        }
                    }
                    NfcWriteState.Erasing -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingIndicator(
                                modifier = Modifier
                                    .size(128.dp)
                                    .fillMaxWidth()
                            )

                            LargeText(text = stringResource(R.string.erasing))
                        }
                    }
                    is NfcWriteState.EraseSuccess -> {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                modifier = Modifier.size(128.dp),
                                imageVector = TablerIcons.CircleCheck,
                                contentDescription = null
                            )

                            LargeText(text = stringResource(R.string.reset_success))
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}