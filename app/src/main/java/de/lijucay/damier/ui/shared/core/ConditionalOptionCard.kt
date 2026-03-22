package de.lijucay.damier.ui.shared.core

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ConditionalOptionsCard(
    modifier: Modifier = Modifier,
    showOption: Boolean,
    parentLayout: @Composable BoxScope.() -> Unit,
    optionLayout: @Composable BoxScope.() -> Unit,
    parentPadding: PaddingValues = PaddingValues(0.dp),
    optionPadding: PaddingValues = PaddingValues(16.dp),
    parentCardColor: CardColors = CardDefaults.cardColors(
        containerColor = colorScheme.primaryContainer,
        contentColor = colorScheme.onPrimaryContainer
    ),
    optionCardColor: CardColors = CardDefaults.cardColors(
        containerColor = colorScheme.secondaryContainer,
        contentColor = colorScheme.onSecondaryContainer
    ),
    onParentClicked: () -> Unit,
    onOptionClicked: () -> Unit
) {
    val animatedCornerSize by animateDpAsState(
        targetValue = if (showOption) 4.dp else 28.dp,
        animationSpec = motionScheme.defaultSpatialSpec()
    )

    Column(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.extraLarge.copy(
                bottomEnd = CornerSize(animatedCornerSize),
                bottomStart = CornerSize(animatedCornerSize)
            ),
            onClick = onParentClicked,
            content = {
                Box(
                    modifier = Modifier.padding(parentPadding),
                    content = parentLayout
                )
            },
            colors = parentCardColor
        )

        Spacer(modifier = Modifier.height(2.dp))

        AnimatedVisibility(
            visible = showOption
        ) {
            Card(
                onClick = onOptionClicked,
                shape = shapes.extraLarge.copy(
                    topStart = CornerSize(animatedCornerSize),
                    topEnd = CornerSize(animatedCornerSize)
                ),
                content = {
                    Box(
                        modifier = Modifier.padding(optionPadding),
                        content = optionLayout
                    )
                },
                colors = optionCardColor
            )
        }
    }
}