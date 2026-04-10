package de.lijucay.damier.core.presentation.dialogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.DeletionMode

@Composable
fun DeletionDialog(
    mode: DeletionMode,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null
            )
        },
        title = {
            Text(text = stringResource(R.string.are_you_sure))
        },
        text = {
            Text(
                text = stringResource(
                    id = when (mode) {
                        is DeletionMode.Activity -> R.string.deletion_information_activity
                        is DeletionMode.CheckIn -> R.string.deletion_information_check_in
                    }
                )
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.errorContainer,
                    contentColor = colorScheme.onErrorContainer
                )
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.errorContainer,
                    contentColor = colorScheme.onErrorContainer
                )
            ) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        containerColor = colorScheme.errorContainer,
        iconContentColor = colorScheme.onErrorContainer,
        titleContentColor = colorScheme.onErrorContainer,
        textContentColor = colorScheme.onErrorContainer,
    )
}