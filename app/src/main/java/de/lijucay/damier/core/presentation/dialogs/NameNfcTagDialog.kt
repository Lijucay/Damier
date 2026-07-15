package de.lijucay.damier.core.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import compose.icons.TablerIcons
import compose.icons.tablericons.Pencil
import de.lijucay.damier.R
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeTitleText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameNfcTagDialog(
    label: String,
    onSaveName: (name: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val nfcTagName = rememberTextFieldState(initialText = label)

    val untitled = stringResource(R.string.untitled)

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 720.dp)
                .padding(16.dp),
            shape = AlertDialogDefaults.shape
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = TablerIcons.Pencil,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(8.dp))

                LargeTitleText(text = stringResource(R.string.name_your_tag))

                Spacer(modifier = Modifier.height(8.dp))

                DefaultText(text = stringResource(R.string.name_your_tag_info))

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = nfcTagName,
                    placeholder = { Text(untitled) },
                    shape = RoundedCornerShape(100)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(stringResource(android.R.string.cancel))
                    }
                    TextButton(
                        onClick = {
                            onSaveName(
                                nfcTagName
                                    .text
                                    .toString()
                                    .takeIf { it.isNotBlank() }
                                    ?: untitled
                            )
                        },
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}