package de.lijucay.damier.activity_details.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.cue.NfcWriteState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NfcWriteDialog(
    writeState: NfcWriteState,
    onDismiss: () -> Unit
) {
    val autoClosingStates = setOf(
        NfcWriteState.InsufficientSize,
        NfcWriteState.NotNdefCompatible,
        NfcWriteState.NotWriteable,
        NfcWriteState.TagLost,
        NfcWriteState.Unknown
    )

    if (writeState in autoClosingStates) {
        LaunchedEffect(writeState) {
            delay(5000L)
            onDismiss()
        }
    }

    BasicAlertDialog(onDismissRequest = {  }) {
        Surface(
            modifier =
                Modifier
                    .requiredWidth(360.dp)
                    .heightIn(max = 360.dp),
            shape = AlertDialogDefaults.shape,
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
                        NfcWriteState.InsufficientSize -> {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxWidth(),
                                    imageVector = Icons.Rounded.Error,
                                    contentDescription = null
                                )

                                Text(
                                    text = stringResource(R.string.insufficient_size)
                                )
                            }
                        }
                        NfcWriteState.NotNdefCompatible -> {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxWidth(),
                                    imageVector = Icons.Rounded.Error,
                                    contentDescription = null
                                )

                                Text(
                                    text = stringResource(R.string.not_compatible)
                                )
                            }
                        }
                        NfcWriteState.NotWriteable -> {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxWidth(),
                                    imageVector = Icons.Rounded.Error,
                                    contentDescription = null
                                )

                                Text(
                                    text = stringResource(R.string.not_writeable)
                                )
                            }
                        }
                        NfcWriteState.TagLost -> {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    modifier = Modifier.fillMaxWidth(),
                                    imageVector = Icons.Rounded.Warning,
                                    contentDescription = null
                                )

                                Text(
                                    text = stringResource(R.string.tag_lost)
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
                                    modifier = Modifier.fillMaxWidth(),
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = null
                                )

                                Text(
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
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(text = stringResource(R.string.waiting_for_tag))
                            }
                        }
                        NfcWriteState.Writing -> {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LoadingIndicator(
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Text(text = stringResource(R.string.writing))
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}