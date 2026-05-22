package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import de.lijucay.damier.R
import de.lijucay.damier.design.components.SmallText

@Composable
fun Stepper(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onValidationChange: (Boolean) -> Unit = {},
    unit: String
) {
    val isValid = state.text.isEmpty() || state.text.toString().toIntOrNull()?.let { it > 0 } == true

    LaunchedEffect(state.text) {
        onValidationChange(isValid)
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(
            onClick = {
                val current = state.text.toString().toIntOrNull() ?: 1
                if (current > 1) {
                    state.edit { replace(0, length, (current - 1).toString()) }
                }
            },
            enabled = (state.text.toString().toIntOrNull() ?: 1) > 1 && enabled,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = stringResource(R.string.decrease)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BasicTextField(
                state = state,
                enabled = enabled,
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                inputTransformation = InputTransformation.maxLength(9).then {
                    if (!asCharSequence().all { it.isDigit() }) {
                        revertAllChanges()
                    }
                },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = color
                ),
                cursorBrush = SolidColor(color),
                decorator = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        if (state.text.isEmpty()) {
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = color.copy(0.3f),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            AnimatedContent(
                targetState = unit,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                }
            ) { text ->
                SmallText(
                    text = text,
                    color = color.copy(alpha = if (!enabled) 0.5f else 1f)
                )
            }
        }

        FilledIconButton(
            onClick = {
                val current = state.text.toString().toIntOrNull() ?: 1
                if (current < Int.MAX_VALUE) {
                    state.edit { replace(0, length, (current + 1).toString()) }
                }
            },
            enabled = (state.text.toString().toIntOrNull() ?: 1) < Int.MAX_VALUE && enabled,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.increase)
            )
        }
    }
}