package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.adaptiveHorizontalCutoutPadding

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptivePane(
    modifier: Modifier = Modifier,
    scaffoldNavigator: ThreePaneScaffoldNavigator<Any>,
    listPane: @Composable (BoxScope.() -> Unit),
    detailPane: @Composable (BoxScope.() -> Unit),
    showDetailPane: Boolean
) {
    val paneExpansionState = rememberPaneExpansionState(keyProvider = scaffoldNavigator.scaffoldValue)

    NavigableListDetailPaneScaffold(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .adaptiveHorizontalCutoutPadding(
                start = if(showDetailPane) 15.dp else 0.dp,
                end = if (showDetailPane) 15.dp else 0.dp,
                bottom = if (showDetailPane) 15.dp else 0.dp
            ),
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                Box(content = listPane)
            }
        },
        detailPane = {
            AnimatedPane {
                Box(content = detailPane)
            }
        },
        paneExpansionDragHandle = {
            val interactionSource = remember { MutableInteractionSource() }

            VerticalDragHandle(
                modifier = Modifier
                    .paneExpansionDraggable(
                        state = paneExpansionState,
                        minTouchTargetSize = LocalMinimumInteractiveComponentSize.current,
                        interactionSource = interactionSource
                    ),
                interactionSource = interactionSource
            )
        },
        paneExpansionState = paneExpansionState
    )
}