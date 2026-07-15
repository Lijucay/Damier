package de.lijucay.damier.activity_details.presentation

import android.app.Activity
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowNarrowLeft
import compose.icons.tablericons.ArrowNarrowRight
import compose.icons.tablericons.Bolt
import compose.icons.tablericons.ChartBar
import compose.icons.tablericons.Edit
import compose.icons.tablericons.Trash
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.components.CheckInItem
import de.lijucay.damier.activity_details.presentation.components.StreakCard
import de.lijucay.damier.activity_details.presentation.components.nfcChipItem
import de.lijucay.damier.activity_details.presentation.stats.StatsBottomSheet
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.domain.DeletionMode
import de.lijucay.damier.core.presentation.DamierMenu
import de.lijucay.damier.core.presentation.components.Cell
import de.lijucay.damier.core.presentation.components.CookieButton
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.components.WaffleDiagram
import de.lijucay.damier.core.presentation.dialogs.CheckInHistory
import de.lijucay.damier.core.presentation.dialogs.DeletionDialog
import de.lijucay.damier.core.presentation.dialogs.NameNfcTagDialog
import de.lijucay.damier.core.presentation.getLongUnitNamesById
import de.lijucay.damier.core.presentation.getShortUnitNamesById
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.cue.NfcWriteState
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.TitleText
import de.lijucay.damier.nfc.NfcManager
import de.lijucay.damier.nfc.NfcViewModel
import de.lijucay.damier.ui.theme.ActivityTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.scope.Scope
import java.time.LocalDate
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActivityDetailsScreen(
    activityId: String,
    scope: Scope,
    onEditActivity: (ActivityUi) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val detailsViewModel = koinViewModel<ActivityDetailsViewModel>()
    val nfcViewModel = koinViewModel<NfcViewModel>(scope = scope)
    val uiViewModel = koinViewModel<UIViewModel>()
    val nfcManager = koinInject<NfcManager>(scope = scope)

    LaunchedEffect(Unit) {
        activityListViewModel.observeSelectedActivity(UUID.fromString(activityId))
    }

    val selectedActivity by activityListViewModel.selectedActivity.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val isHeightAtLeastExpanded by uiViewModel.isHeightAtLeastExpanded.collectAsStateWithLifecycle()

    val state by detailsViewModel.state.collectAsStateWithLifecycle()
    val nfcWriteState by nfcViewModel.nfcWriteState.collectAsStateWithLifecycle()

    val statsDialogState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val activityFormSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val nfcSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val nfcChipListSheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )

    val deletionMode by detailsViewModel.deletionDialogMode.collectAsStateWithLifecycle()
    val nfcLabelState by nfcViewModel.nfcLabelState.collectAsStateWithLifecycle()

    val host = stringResource(R.string.host)

    LaunchedEffect(selectedActivity) {
        selectedActivity?.let {
            detailsViewModel.load(it)
        } ?: detailsViewModel.clear()
    }

    val shouldEnableReaderMode = remember(nfcWriteState) {
        nfcWriteState is NfcWriteState.WaitingForTag ||
        nfcWriteState is NfcWriteState.Writing
    }

    DisposableEffect(shouldEnableReaderMode) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val activityId = selectedActivity?.id ?: return@DisposableEffect onDispose {}

        if (shouldEnableReaderMode) {
            nfcManager.enableReaderMode(activity) { tag ->
                nfcViewModel.onTagDiscovered(tag, host, activityId)
            }
        } else {
            nfcManager.disableReaderMode(activity)
        }

        onDispose {
            nfcManager.disableReaderMode(activity)
        }
    }

    ActivityTheme(useLimitTheme = state.useLimitTheme) {
        ScreenContainer(
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
                        detailsViewModel.setCheckInFormMode(
                            CheckInFormMode.Add(
                                it.id,
                                it.defaultAmount
                            )
                        )
                    }
                }
            },
            showBottomBarContent = isHeightAtLeastExpanded,
            navigationIcon = {
                if (!isWidthAtLeastExpanded) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = TablerIcons.ArrowNarrowLeft,
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
                                        activity.id,
                                        activity.defaultAmount
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = TablerIcons.Bolt,
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
                                    imageVector = TablerIcons.ChartBar,
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
                                onEditActivity(activity)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = TablerIcons.Edit,
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
                                detailsViewModel.setDeletionMode(DeletionMode.Activity(activity))
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = TablerIcons.Trash,
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
                        hasNfc = nfcManager.hasNfc,
                        onShowHistory = {
                            detailsViewModel.setShowHistory(true)
                        },
                        onItemClick = {
                            detailsViewModel.setCheckInFormMode(
                                mode = CheckInFormMode.Edit(it)
                            )
                        },
                        onOpenNfcList = { detailsViewModel.showNfcList(true) }
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
                    sheetState = activityFormSheetState,
                    mode = mode,
                    useLimitTheme = state.useLimitTheme,
                    onDeleteRequest = {
                        scope.launch { activityFormSheetState.hide() }.invokeOnCompletion {
                            detailsViewModel.setCheckInFormMode(null)
                        }
                        detailsViewModel.setDeletionMode(DeletionMode.CheckIn(it, activity)) },
                    onDismissRequest = {
                        scope.launch { activityFormSheetState.hide() }.invokeOnCompletion {
                            detailsViewModel.setCheckInFormMode(null)
                        }
                    },
                    unit = activity.unitId.getLongUnitNamesById(context)
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
                },
                reference = selectedActivity?.reference?.toDouble()
            )
        }

        if (state.showNfcList) {
            NfcChipListDialog(
                sheetState = nfcChipListSheetState,
                nfcChips = state.allNfcChips,
//                useLimitTheme = state.useLimitTheme,
                onUnlinkRequest = { chipId ->
                    detailsViewModel.unlinkNfcChip(chipId)
                },
                onLinkRequest = { nfcViewModel.startNfcWrite() },
                onEditLabelClicked = { chipId, label -> nfcViewModel.setUpdateLabel(chipId, label)},
                onDismissRequest = {
                    scope.launch { nfcChipListSheetState.hide() }.invokeOnCompletion {
                        detailsViewModel.showNfcList(false)
                    }
                }
            )
        }

        deletionMode?.let { mode ->
            DeletionDialog(
                mode = mode,
                onDismissRequest = { detailsViewModel.setDeletionMode(null) },
                onDelete = {
                    when (mode) {
                        is DeletionMode.Activity -> {
                            detailsViewModel.setDeletionMode(null)
                            activityListViewModel.deleteActivity(mode.activity)
                            onNavigateBack()
                        }
                        is DeletionMode.CheckIn -> {
                            detailsViewModel.setDeletionMode(null)
                            activityListViewModel.deleteCheckIn(mode.checkIn)
                        }
                    }
                }
            )
        }

        nfcLabelState.currentNfcTagId?.let { chipId ->
            NameNfcTagDialog(
                label = nfcLabelState.currentLabel,
                onSaveName = { label ->
                    nfcViewModel.updateLabel(label, chipId)
                    nfcViewModel.hideLabelDialog()
                    nfcViewModel.dismissNfcWrite()
                },
                onDismissRequest = {
                    nfcViewModel.hideLabelDialog()
                    nfcViewModel.dismissNfcWrite()
                }
            )
        }

        when (val writeState = nfcWriteState) {
            NfcWriteState.Idle -> {}
            else -> {
                NfcWriteDialog(
                    writeState = writeState,
                    sheetState = nfcSheetState,
                    onDismissRequest = {
                        scope.launch { nfcSheetState.hide() }.invokeOnCompletion {
                            nfcViewModel.dismissNfcWrite()
                        }
                    }
                )
            }
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
    onOpenNfcList: () -> Unit,
) {
    val context = LocalContext.current
    val unitNames = state.unitId.getShortUnitNamesById(context)

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        waffleDiagramItem(state)
        streakItem(state)
        nfcChipItem(state, hasNfc, onOpenNfcList)
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val cDate = LocalDate.now()
                        val eDate = cDate.plusDays(1)

                        Text(
                            stringResource(R.string.less)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Cell(
                            modifier = Modifier.size(16.dp),
                            checkInCount = 1,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 4,
                            type = state.waffleDiagramData.referenceType,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Cell(
                            modifier = Modifier.size(16.dp),
                            checkInCount = 2,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 4,
                            type = state.waffleDiagramData.referenceType,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Cell(
                            modifier = Modifier.size(16.dp),
                            checkInCount = 3,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 4,
                            type = state.waffleDiagramData.referenceType,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Cell(
                            modifier = Modifier.size(16.dp),
                            checkInCount = 4,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 4,
                            type = state.waffleDiagramData.referenceType,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.more)
                        )
                    }
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
                longestStreak = state.longestStreakLength,
                referenceType = state.referenceType
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
                    imageVector = TablerIcons.ArrowNarrowRight,
                    contentDescription = null
                )
            }
        }
    }
}