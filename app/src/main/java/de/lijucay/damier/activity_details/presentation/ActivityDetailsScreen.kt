package de.lijucay.damier.activity_details.presentation

import android.app.Activity
import android.nfc.NfcAdapter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.components.CheckInItem
import de.lijucay.damier.activity_details.presentation.components.StreakCard
import de.lijucay.damier.activity_details.presentation.stats.StatsBottomSheet
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.domain.DeletionMode
import de.lijucay.damier.core.domain.getShortUnitNamesById
import de.lijucay.damier.core.presentation.DamierMenu
import de.lijucay.damier.core.presentation.components.CookieButton
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.components.WaffleDiagram
import de.lijucay.damier.core.presentation.dialogs.CheckInHistory
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText
import de.lijucay.damier.ui.theme.ActivityTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActivityDetailsScreen(
    modifier: Modifier = Modifier,
    onEditActivity: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }

    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val uiViewModel = koinViewModel<UIViewModel>()
    val detailsViewModel = koinViewModel<ActivityDetailsViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val isHeightAtLeastExpanded by uiViewModel.isHeightAtLeastExpanded.collectAsStateWithLifecycle()
    val selectedActivity by activityListViewModel.selectedActivity.collectAsStateWithLifecycle()
    val state by detailsViewModel.state.collectAsStateWithLifecycle()

    val statsDialogState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val host = stringResource(R.string.host)

    LaunchedEffect(selectedActivity) {
        selectedActivity?.let {
            detailsViewModel.load(it)
        } ?: detailsViewModel.clear()
    }


    DisposableEffect(state.nfcWriteState) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}

        if (state.nfcWriteState is NfcWriteState.WaitingForTag) {
            nfcAdapter?.enableReaderMode(
                activity,
                { tag ->
                    val activityId = selectedActivity?.id ?: return@enableReaderMode
                    detailsViewModel.onTagDiscovered(tag, activityId, host)
                },
                NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V,
                null
            )
        } else {
            nfcAdapter?.disableReaderMode(activity)
        }

        onDispose {
            nfcAdapter?.disableReaderMode(activity)
        }
    }

    when (val writeState = state.nfcWriteState) {
        NfcWriteState.Idle -> {}
        is NfcWriteState.Success -> {
            LaunchedEffect(writeState) {
                selectedActivity?.let { activity ->
                    activityListViewModel.updateNfcChipId(activity.id, writeState.chipId)
                }
                detailsViewModel.dismissNfcWrite()
            }
        }
        else -> {
            NfcWriteDialog(
                writeState = writeState,
                onDismiss = { detailsViewModel.dismissNfcWrite() }
            )
        }
    }

    ActivityTheme(useLimitTheme = state.useLimitTheme) {
        ScreenContainer(
            modifier = modifier,
            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
            title = state.title.ifBlank { null },
            bottomBarContent = {
                CookieButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    )
                ) {
                    selectedActivity?.let {
                        detailsViewModel.setCheckInFormMode(CheckInFormMode.Add(it.id))
                    }
                }
            },
            showBottomBarContent = isHeightAtLeastExpanded,
            navigationIcon = {
                if (!isWidthAtLeastExpanded) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            },
            topAppBarActions = {
                selectedActivity?.let { activity ->
                    if (!isHeightAtLeastExpanded) {
                        IconButton(
                            onClick = {
                                detailsViewModel.setCheckInFormMode(
                                    CheckInFormMode.Add(
                                        activity.id
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = null
                            )
                        }
                    }
                    DamierMenu(
                        expanded = state.menuExpanded,
                        onShowMenu = { detailsViewModel.showMenu(it) }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                detailsViewModel.showMenu(false)
                                detailsViewModel.setShowStatsDialog(true)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.QueryStats,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(stringResource(R.string.statistics))
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                detailsViewModel.showMenu(false)
                                onEditActivity()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(stringResource(R.string.edit_activity))
                            }
                        )
                        DropdownMenuItem(
                            onClick = {
                                detailsViewModel.showMenu(false)
                                uiViewModel.setDeletionMode(DeletionMode.Activity(activity))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            text = {
                                Text(
                                    text = stringResource(R.string.delete),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }
        ) {
            AnimatedContent(
                targetState = selectedActivity,
                contentKey = { it?.id },
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                }
            ) { activity ->
                if (activity != null) {
                    ActivityDetails(
                        state = state,
                        hasNfc = nfcAdapter != null,
                        onShowHistory = {
                            detailsViewModel.setShowHistory(true)
                        },
                        onItemClick = {
                            detailsViewModel.setCheckInFormMode(
                                mode = CheckInFormMode.Edit(it)
                            )
                        },
                        onWriteNfcChip = {
                            if (nfcAdapter != null) detailsViewModel.startNfcWrite()
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        DefaultText(text = stringResource(R.string.no_activity_selected))
                    }
                }
            }
        }

        state.checkInFormMode?.let { mode ->
            selectedActivity?.let { activity ->
                CheckInForm(
                    onDismissRequest = { detailsViewModel.setCheckInFormMode(null) },
                    mode = mode,
                    useLimitTheme = state.useLimitTheme,
                    onDeleteRequest = { uiViewModel.setDeletionMode(DeletionMode.CheckIn(it, activity)) }
                )
            }
        }

        if (state.showCheckInHistory) {
            CheckInHistory(
                sheetState = sheetState,
                checkIns = state.allCheckIns,
                unitId = state.unitId,
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        detailsViewModel.setShowHistory(false)
                    }
                }
            ) {
                detailsViewModel.setCheckInFormMode(
                    mode = CheckInFormMode.Edit(it)
                )
            }
        }

        if (state.showStatsDialog) {
            StatsBottomSheet(
                state = state,
                dialogState = statsDialogState,
                onDismissRequest = {
                    scope.launch { statsDialogState.hide() }.invokeOnCompletion {
                        detailsViewModel.setShowStatsDialog(false)
                    }
                }
            )
        }
    }
}


@Composable
private fun ActivityDetails(
    modifier: Modifier = Modifier,
    state: ActivityDetailsState,
    hasNfc: Boolean,
    onShowHistory: () -> Unit,
    onItemClick: (CheckInUi) -> Unit,
    onWriteNfcChip: () -> Unit
) {
    val context = LocalContext.current
    val unitNames = state.unitId.getShortUnitNamesById(context)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        waffleDiagramItem(state)
        streakItem(state)
        nfcChipItem(state, hasNfc, onWriteNfcChip)
        todayHeaderItem(state, unitNames.shortUnitSingular, unitNames.shortUnitPlural)
        checkInItems(
            state,
            unitNames.shortUnitSingular,
            unitNames.shortUnitPlural,
            onItemClick = onItemClick
        )
        checkInHistory(state, onClick = onShowHistory)
    }
}


private fun LazyListScope.waffleDiagramItem(state: ActivityDetailsState) {
    item {
        state.waffleDiagramData ?: return@item
        Column {
            Card(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = contentColorFor(MaterialTheme.colorScheme.primaryContainer)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WaffleDiagram(waffleDiagramData = state.waffleDiagramData)
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

private fun LazyListScope.streakItem(state: ActivityDetailsState) {
    item {
        if (!state.showStreakCard) return@item
        Column {
            StreakCard(
                modifier = Modifier
                    .animateItem()
                    .padding(top = 8.dp),
                currentStreak = state.currentStreakLength,
//                longestStreak = state.longestStreakLength
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

private fun LazyListScope.todayHeaderItem(
    state: ActivityDetailsState,
    singularName: String,
    pluralName: String,
) {
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .animateItem()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TitleText(text = stringResource(R.string.today))

            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                val unitName = if (state.todaysTotal == 1) singularName else pluralName
                val displayText = "${state.todaysTotal} $unitName"

                AnimatedContent(
                    targetState = displayText,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                    }
                ) { text ->
                    DefaultText(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = text,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

private fun LazyListScope.checkInItems(
    state: ActivityDetailsState,
    singularName: String,
    pluralName: String,
    onItemClick: (CheckInUi) -> Unit
) {
    if (state.todaysCheckIns.isEmpty()) {
        item {
            Box(
                modifier = Modifier
                    .animateItem()
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                LargeText(
                    text = stringResource(R.string.nothing_logged_yet),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        items(state.todaysCheckIns) { checkIn ->
            CheckInItem(
                modifier = Modifier
                    .animateItem(),
                checkInUi = checkIn,
                isItemFirst = state.todaysCheckIns.first() == checkIn,
                isItemLast = state.todaysCheckIns.last() == checkIn,
                unitName = if (checkIn.amount > 1) pluralName else singularName,
                onClick = { onItemClick(checkIn) }
            )
        }
    }
}

private fun LazyListScope.checkInHistory(
    state: ActivityDetailsState,
    onClick: () -> Unit
) {
    item {
        Card(
            modifier = Modifier
                .animateItem(),
            shape = RoundedCornerShape(
                topStart = 28.dp,
                topEnd = 28.dp,
                bottomEnd = 28.dp,
                bottomStart = 28.dp
            ),
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = if (state.useLimitTheme)
                    MaterialTheme.colorScheme.errorContainer
                else
                    MaterialTheme.colorScheme.primaryContainer,
                contentColor = if (state.useLimitTheme)
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultText(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.full_history)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }
}

private fun LazyListScope.nfcChipItem(
    state: ActivityDetailsState,
    hasNfc: Boolean,
    onWriteNfcChip: () -> Unit
) {
    if (!hasNfc) return

    item {
        Card(
            modifier = Modifier
                .animateItem()
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            onClick = onWriteNfcChip,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DefaultText(
                        text = stringResource(
                            id = if (state.nfcChipId != null)
                                R.string.nfc_chip_linked
                            else
                                R.string.link_nfc_chip
                        ),
                        fontWeight = FontWeight.Bold
                    )

                    if (state.nfcChipId != null) {
                        SmallText(
                            text = state.nfcChipId,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}