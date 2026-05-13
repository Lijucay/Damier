package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.design.components.SmallText

@Composable
fun Stepper(
    modifier: Modifier = Modifier,
    value: Int,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onValueChange: (Int) -> Unit,
    unit: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FilledIconButton(
            onClick = { if (value > 1) onValueChange(value - 1)},
            enabled = value > 1 && enabled,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = stringResource(R.string.decrease)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedContent(
                targetState = value,
                transitionSpec = {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                }
            ) { text ->
                LargeTitleText(
                    text = text.toString(),
                    color = color.copy(alpha = if (!enabled) 0.5f else 1f)
                )
            }

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
            onClick = { if (value < Int.MAX_VALUE) onValueChange(value + 1)},
            enabled = value < Int.MAX_VALUE && enabled,
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(R.string.increase)
            )
        }
    }
}