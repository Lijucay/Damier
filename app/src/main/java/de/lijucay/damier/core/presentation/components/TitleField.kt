package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.design.components.DefaultText

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TitleField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = { DefaultText(text = stringResource(R.string.title_eg)) },
        value = value,
        onValueChange = onValueChange,
        shape = shapes.extraLargeIncreased,
        keyboardOptions = KeyboardOptions()
    )
}