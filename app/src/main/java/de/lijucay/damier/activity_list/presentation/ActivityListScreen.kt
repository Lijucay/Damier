package de.lijucay.damier.activity_list.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActivityListScreen(
    modifier: Modifier = Modifier,
    onActivityClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onAddActivity: () -> Unit
) {
    val uiViewModel = koinViewModel<UIViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.app_name),
        topAppBarActions = {
            IconButton(
                onClick = onSettingsClicked
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
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
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_activity)
                )
            }
        }
    ) {
        ActivityList(onActivityClicked = { onActivityClicked() })
    }
}
