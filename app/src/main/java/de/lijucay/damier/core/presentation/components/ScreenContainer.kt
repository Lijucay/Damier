package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.animateClipWithScreenSize
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.clipInnerContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    title: String? = null,
    isWidthAtLeastExpanded: Boolean,
    bottomBarContent: @Composable (BoxScope.() -> Unit) = {},
    showBottomBarContent: Boolean = false,
    topAppBarActions: @Composable (RowScope.() -> Unit) = {},
    showFloatingActionButton: Boolean = true,
    floatingActionButton: (@Composable () -> Unit) = {},
    navigationIcon: @Composable (() -> Unit) = {},
    snackbarHost: @Composable (() -> Unit) = {},
    content: @Composable (BoxScope.() -> Unit)
) {
    Scaffold(
        modifier = modifier
            .animateContentSize(
                animationSpec = motionScheme.defaultSpatialSpec()
            ),
        contentWindowInsets = WindowInsets
            .displayCutout
            .exclude(WindowInsets.safeContent)
            .add(WindowInsets.statusBars),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        snackbarHost = snackbarHost,
        topBar = {
            title?.let { title ->
                CenterAlignedTopAppBar(
                    title = {
                        AnimatedContent(
                            targetState = title,
                            transitionSpec = {
                                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                            }
                        ) { animatedTitle ->
                            Text(text = animatedTitle.ifBlank { stringResource(R.string.app_name) })
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        titleContentColor = contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ),
                    actions = topAppBarActions,
                    navigationIcon = navigationIcon
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBarContent,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                    content = bottomBarContent
                )
            }
        },
        floatingActionButton = {
            AnimatedVisibility(visible = showFloatingActionButton) {
                floatingActionButton()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .animateClipWithScreenSize(isWidthAtLeastExpanded, showBottomBar = showBottomBarContent)
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(
                        top = 16.dp,
                        end = 16.dp,
                        bottom = if (isWidthAtLeastExpanded) 16.dp else bottomPadding(),
                        start = 16.dp
                    )
                    .fillMaxSize()
                    .clipInnerContainer()
                    .background(MaterialTheme.colorScheme.surface)
                    .animateContentSize(),
                content = content
            )
        }
    }
}