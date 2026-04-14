package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun NumberTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    cursorBrush: Brush = SolidColor(colorScheme.primary),
    textColor: Color = colorScheme.primary,
    focusRequester: FocusRequester,
) {
    BasicTextField(
        modifier = Modifier
            .width(32.dp)
            .focusRequester(focusRequester),
        value = value,
        onValueChange = onValueChange,
        cursorBrush = cursorBrush,
        textStyle = typography.bodyLarge.copy(
            color = textColor,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        )
    )
}