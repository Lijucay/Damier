package dev.lijucay.damier.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.lijucay.damier.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncrementCountDialog(
    sheetState: SheetState,
    onSavePressed: (Int) -> Unit, // Unit counts that need to be added
    habitTitle: String,
    customCount: Int? = null,
    onDismissRequest: () -> Unit
) {
    var count by remember { mutableIntStateOf(customCount ?: 1) } // by default, we only want to add
    // one unit to our habit
    var countString by remember { mutableStateOf(count.toString()) }
    var saveButtonEnabled by remember { mutableStateOf(true) }
    var showErrorText by remember { mutableStateOf(false) }

    LaunchedEffect(count) { countString = count.toString() }
    LaunchedEffect(countString) {
        countString.toIntOrNull()?.let {
            count = it
            saveButtonEnabled = true
            showErrorText = false
        } ?: run {
            saveButtonEnabled = false
            showErrorText = true
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        val uiSystemController = rememberSystemUiController()

        uiSystemController.setNavigationBarColor(Color.Transparent)
        uiSystemController.isNavigationBarContrastEnforced = false

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onDismissRequest,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }

                Text(
                    text = habitTitle,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { onSavePressed(count) },
                    enabled = saveButtonEnabled,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Save,
                        contentDescription = null
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { count-- },
                    enabled = count > 0
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = stringResource(R.string.remove)
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.width(100.dp),
                    value = countString,
                    onValueChange = { newValue -> countString = newValue },
                    isError = showErrorText,
                    supportingText = {
                        if (showErrorText) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.must_be_digit),
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )

                IconButton(onClick = { count++ }) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                }
            }
        }
    }
}