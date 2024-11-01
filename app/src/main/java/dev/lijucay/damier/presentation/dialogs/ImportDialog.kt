package dev.lijucay.damier.presentation.dialogs

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.lijucay.damier.R
import dev.lijucay.damier.presentation.viewmodels.UIViewModel

@Composable
fun ImportDialog(
    onDismiss: () -> Unit,
    onComplete: (Boolean) -> Unit,
    uiViewModel: UIViewModel,
    fileUri: Uri
) {
    var totalCount by remember { mutableIntStateOf(-1) }
    var currentCount by remember { mutableIntStateOf(0) }
    var currentProgress by remember { mutableIntStateOf(0) }

    uiViewModel.importData(
        fileUri = fileUri,
        onTotalCountUpdate = {
            totalCount = it
        },
        onCurrentCountUpdate = {
            currentCount = it
            currentProgress = if (totalCount > 0) (it * 100) / totalCount else 0
        },
        onComplete = onComplete
    )

    Dialog(onDismissRequest = {  }) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AnimatedContent(
                modifier = Modifier.padding(16.dp),
                targetState = totalCount == -1,
                label = "Import Progress"
            ) { state ->
                if (state) {
                    CircularProgressIndicator()
                } else {
                    Column {
                        Text(text = stringResource(R.string.import_data))
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = { currentProgress.toFloat() }
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            text = "$currentCount/$totalCount"
                        )
                    }
                }
            }
        }
    }
}