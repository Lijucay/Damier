package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import de.lijucay.damier.design.components.TitleText

/**
 *
 * @param arrowContent Content that is shown before the arrow
 * */
@Composable
fun ExpandableLayout(
    modifier: Modifier = Modifier,
    title: String,
    expanded: Boolean,
    alwaysShowArrowContent: Boolean = false,
    arrowContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val animatedArrowRotation by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f,
        animationSpec = motionScheme.defaultSpatialSpec()
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(text = title)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedVisibility(visible = !expanded || alwaysShowArrowContent) {
                    arrowContent()
                }
                Icon(
                    modifier = Modifier
                        .rotate(animatedArrowRotation),
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = expanded
        ) {
            content()
        }
    }
}