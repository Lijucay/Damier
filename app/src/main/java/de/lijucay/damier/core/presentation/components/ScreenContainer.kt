package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.animateContentSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.clipWithScreenSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenContainer(
    modifier: Modifier = Modifier,
    title: String? = null,
    isWidthAtLeastExpanded: Boolean,
    bottomBarContent: (@Composable (BoxScope.() -> Unit))? = null,
    showBottomBarContent: Boolean = false,
    topAppBarActions: @Composable (RowScope.() -> Unit) = {},
    floatingActionButton: (@Composable () -> Unit) = {},
    content: @Composable (BoxScope.() -> Unit)
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets
            .displayCutout
            .exclude(WindowInsets.safeContent)
            .add(WindowInsets.statusBars),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            title?.let { title ->
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = title.ifBlank { stringResource(R.string.app_name) })
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        titleContentColor = contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ),
                    actions = topAppBarActions
                )
            }
        },
        bottomBar = {
            if (bottomBarContent != null && showBottomBarContent) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                    content = bottomBarContent
                )
            }
        },
        floatingActionButton = floatingActionButton
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clipWithScreenSize(isWidthAtLeastExpanded, isHeightAtLeastExpanded = false)
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize(),
            content = content
        )
    }
}