package de.lijucay.damier.core.presentation.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.InfoMode

@Composable
fun InfoDialog(
    mode: InfoMode,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text(
                text = stringResource(
                    id = when (mode) {
                        is InfoMode.BackupError -> R.string.back_up_error
                        InfoMode.BackupSuccess, InfoMode.ImportSuccess -> R.string.success
                        is InfoMode.ImportError -> R.string.import_error
                    }
                )
            )
        },
        text = {
            Text(
                text = when (mode) {
                        is InfoMode.BackupError -> mode.message ?: stringResource(R.string.unknown_error)
                        InfoMode.BackupSuccess -> stringResource(R.string.backup_successful)
                        InfoMode.ImportSuccess -> stringResource(R.string.import_successful)
                        is InfoMode.ImportError -> mode.message ?: stringResource(R.string.unknown_error)
                    }
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(android.R.string.ok))
            }
        }
    )
}