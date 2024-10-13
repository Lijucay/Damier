package dev.lijucay.damier.presentation.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.lijucay.damier.presentation.composables.MediumTitleText
import dev.lijucay.damier.presentation.viewmodels.UIViewModel

@Composable
fun InfoDialog(
    uiViewModel: UIViewModel,
    onDismissRequest: () -> Unit
) {
    val scrollState = rememberScrollState()
    val title by uiViewModel.infoDialogTitle.collectAsState()
    val message by uiViewModel.infoDialogMessage.collectAsState()

    Dialog(onDismissRequest = onDismissRequest) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 5.dp,
                    vertical = 36.dp
                ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                MediumTitleText(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = title
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .fillMaxWidth(),
                    text = message
                )
                Spacer(Modifier.height(16.dp))
                TextButton(onClick = onDismissRequest) {
                    Text(text = stringResource(android.R.string.ok))
                }
            }
        }
    }
}