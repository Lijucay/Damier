package de.lijucay.damier.activity_list.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowDown
import compose.icons.tablericons.ArrowUp
import compose.icons.tablericons.ArrowsSort
import compose.icons.tablericons.Check
import compose.icons.tablericons.Layout
import compose.icons.tablericons.Plus
import compose.icons.tablericons.Settings
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.DamierMenu
import de.lijucay.damier.core.presentation.DisplayMode
import de.lijucay.damier.core.presentation.SortMode
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

    var showDisplayModeMenu by remember { mutableStateOf(false) }
    var showSortModeMenu by remember { mutableStateOf(false) }

    val displayMode by uiViewModel.displayMode.collectAsStateWithLifecycle()
    val sortMode by uiViewModel.sortMode.collectAsStateWithLifecycle()

    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        snackbarHost = snackbarHost,
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.app_name),
        topAppBarActions = {
            Box {
                DamierMenu(
                    icon = TablerIcons.Layout,
                    expanded = showDisplayModeMenu,
                    onShowMenu = { showDisplayModeMenu = it }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            uiViewModel.setDisplayMode(DisplayMode.EXPANDED)
                        },
                        trailingIcon = {
                            if (DisplayMode.valueOf(displayMode).isExpanded()) {
                                Icon(
                                    imageVector = TablerIcons.Check,
                                    contentDescription = null
                                )
                            }
                        },
                        text = {
                            Text(stringResource(R.string.expanded))
                        }
                    )

                    DropdownMenuItem(
                        onClick = {
                            uiViewModel.setDisplayMode(DisplayMode.COMPACT)
                        },
                        trailingIcon = {
                            if (DisplayMode.valueOf(displayMode).isCompact()) {
                                Icon(
                                    imageVector = TablerIcons.Check,
                                    contentDescription = null
                                )
                            }
                        },
                        text = {
                            Text(stringResource(R.string.compact))
                        }
                    )

                    DropdownMenuItem(
                        onClick = {
                            uiViewModel.setDisplayMode(DisplayMode.EXTRA_COMPACT)
                        },
                        trailingIcon = {
                            if (DisplayMode.valueOf(displayMode).isExtraCompact()) {
                                Icon(
                                    imageVector = TablerIcons.Check,
                                    contentDescription = null
                                )
                            }
                        },
                        text = {
                            Text(stringResource(R.string.extra_compact))
                        }
                    )
                }
            }

            DamierMenu(
                icon = TablerIcons.ArrowsSort,
                expanded = showSortModeMenu,
                onShowMenu = { showSortModeMenu = it }
            ) {
                DropdownMenuItem(
                    onClick = {
                        uiViewModel.setSortMode(
                            if (SortMode.valueOf(sortMode).isNameAsc()) SortMode.NAME_DESC
                            else SortMode.NAME_ASC
                        )
                    },
                    trailingIcon = {
                        if (SortMode.valueOf(sortMode).isNameDesc() || SortMode.valueOf(sortMode).isNameAsc())
                            Icon(
                                imageVector = if (SortMode.valueOf(sortMode).isNameAsc())
                                    TablerIcons.ArrowDown
                                else TablerIcons.ArrowUp,
                                contentDescription = null
                            )
                    },
                    text = {
                        Text(stringResource(R.string.name))
                    }
                )

                DropdownMenuItem(
                    onClick = {
                        uiViewModel.setSortMode(SortMode.RECENTLY_CHECKED_IN)
                    },
                    trailingIcon = {
                        if (SortMode.valueOf(sortMode).isRecentlyCheckedIn())
                            Icon(
                                imageVector = TablerIcons.Check,
                                contentDescription = null
                            )
                    },
                    text = {
                        Text(stringResource(R.string.recently_checked_in))
                    }
                )
            }

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
        },
    ) {
        ActivityList(
            onActivityClicked = { onActivityClicked(it) },
            uiViewModel = uiViewModel
        )
    }
}
