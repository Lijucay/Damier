package de.lijucay.damier.core.presentation.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.InfoAction
import de.lijucay.damier.core.domain.InfoMode
import de.lijucay.damier.core.presentation.components.text.DefaultText
import de.lijucay.damier.core.presentation.components.text.TitleText

@Composable
fun InfoDialog(
    mode: InfoMode,
    onAction: (InfoAction) -> Unit = {},
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            TitleText(
                text = stringResource(
                    id = when (mode) {
                        is InfoMode.BackupError -> R.string.back_up_error
                        InfoMode.BackupSuccess, InfoMode.ImportSuccess -> R.string.success
                        is InfoMode.ImportError -> R.string.import_error
                        InfoMode.NotificationPermissionNotGranted -> R.string.notifications_not_granted
                    }
                )
            )
        },
        text = {
            DefaultText(
                text = when (mode) {
                        is InfoMode.BackupError -> mode.message ?: stringResource(R.string.unknown_error)
                        InfoMode.BackupSuccess -> stringResource(R.string.backup_successful)
                        InfoMode.ImportSuccess -> stringResource(R.string.import_successful)
                        is InfoMode.ImportError -> mode.message ?: stringResource(R.string.unknown_error)
                        InfoMode.NotificationPermissionNotGranted -> stringResource(R.string.notifications_not_granted_message)
                    }
            )
        },
        onDismissRequest = onDismissRequest,
        dismissButton = {
            if (mode == InfoMode.NotificationPermissionNotGranted)
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    DefaultText(text = stringResource(android.R.string.ok))
                }
        },
        confirmButton = {
            if (mode != InfoMode.NotificationPermissionNotGranted)
                TextButton(
                    onClick = onDismissRequest,
                ) {
                    DefaultText(text = stringResource(android.R.string.ok))
                }
            else
                TextButton(
                    onClick = { onAction(InfoAction.GrantNotificationPermission) },
                ) {
                    DefaultText(text = stringResource(R.string.grant))
                }
        }
    )
}