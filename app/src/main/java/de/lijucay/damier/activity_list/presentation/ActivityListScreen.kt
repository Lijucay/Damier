package de.lijucay.damier.activity_list.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Settings
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.util.UUID

@Composable
fun ActivityListScreen(
    modifier: Modifier = Modifier,
    snackbarHost: @Composable (() -> Unit),
    onActivityClicked: (UUID) -> Unit,
    onSettingsClicked: () -> Unit,
    onAddActivity: () -> Unit
) {
    val uiViewModel = koinViewModel<UIViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        snackbarHost = snackbarHost,
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.app_name),
        topAppBarActions = {
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    imageVector = TablerIcons.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.paddingWithSafeNavigationBar(),
                onClick = onAddActivity
            ) {
                Icon(
                    imageVector = TablerIcons.Plus,
                    contentDescription = stringResource(R.string.add_activity)
                )
            }
        }
    ) {
        ActivityList(
            onActivityClicked = { onActivityClicked(it) },
            uiViewModel = uiViewModel
        )
    }
}
