package de.lijucay.damier.core.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.domain.ActivityFormMode
import de.lijucay.damier.core.presentation.viewmodels.ActivityFormViewModel
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.core.presentation.components.cards.DefaultAmountCard
import de.lijucay.damier.core.presentation.components.cards.PreviewCard
import de.lijucay.damier.core.presentation.components.cards.ReferenceCard
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.components.TitleField
import de.lijucay.damier.core.presentation.components.cards.UnitCard
import de.lijucay.damier.design.components.HeadlineText
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.ui.theme.ActivityTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ActivityFormScreen(
    modifier: Modifier = Modifier,
    mode: ActivityFormMode,
    onNavigateBack: () -> Unit
) {
    val uiViewModel = koinViewModel<UIViewModel>()
    val activityListViewModel = koinViewModel<ActivityListViewModel>()
    val formViewModel = koinViewModel<ActivityFormViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()
    val state by formViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(mode) {
        when (mode) {
            is ActivityFormMode.Add -> formViewModel.initForAdd()
            is ActivityFormMode.Edit -> formViewModel.initForEdit(mode.activity)
        }
    }

    val screenTitle = when (mode) {
        is ActivityFormMode.Add -> stringResource(R.string.add_activity)
        is ActivityFormMode.Edit -> stringResource(R.string.edit, mode.activity.title)
    }

    val focusManager = LocalFocusManager.current
    val defaultAmountFocusRequester = remember { FocusRequester() }
    val referenceFocusRequester = remember { FocusRequester() }

    ActivityTheme(state.useLimitTheme) {
        ScreenContainer(
            modifier = modifier.fillMaxSize(),
            isWidthAtLeastExpanded = isWidthAtLeastExpanded,
            title = screenTitle,
            topAppBarActions = {
                FilledIconButton(
                    enabled = state.isSaveEnabled,
                    onClick = {
                        activityListViewModel.upsert(formViewModel.buildActivityInfo())
                        onNavigateBack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = stringResource(R.string.save)
                    )
                }
            },
            navigationIcon = {
                if (!isWidthAtLeastExpanded) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                PreviewCard(state = state)

                Spacer(Modifier.height(16.dp))

                TitleField(
                    value = state.title,
                    onValueChange = formViewModel::setTitle
                )

                Spacer(Modifier.height(16.dp))

                LargeTitleText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.tracking_options),
                )

                Spacer(Modifier.height(8.dp))

                UnitCard(
                    state = state,
                    onUseUnitsToggle = { formViewModel.setUseUnits(!state.useUnits) },
                    onShowUnitsToggle = { formViewModel.setShowUnits(!state.showUnits) },
                    onUnitSelected = formViewModel::setUnitId
                )

                Spacer(Modifier.height(8.dp))

                AnimatedVisibility(visible = state.useUnits) {
                    DefaultAmountCard(
                        state = state,
                        focusRequester = defaultAmountFocusRequester,
                        onUseDefaultAmountToggle = { formViewModel.setUseDefaultAmount(!state.useDefaultAmount) },
                        onValueChange = formViewModel::setDefaultAmount,
                        onFocusRequested = {
                            focusManager.clearFocus()
                            formViewModel.setDefaultAmount(
                                formViewModel.focusDefaultAmount(state.defaultAmount)
                            )
                            defaultAmountFocusRequester.requestFocus()
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))

                ReferenceCard(
                    state = state,
                    focusRequester = referenceFocusRequester,
                    onUseReferenceToggle = { formViewModel.setUseReference(!state.useReference) },
                    onShowReferenceTypesToggle = {
                        formViewModel.setReference(
                            formViewModel.focusReference(state.reference)
                        )
                        formViewModel.setShowReferenceTypes(!state.showReferenceTypes)
                    },
                    onFocusRequested = {
                        focusManager.clearFocus()
                        referenceFocusRequester.requestFocus()
                    },
                    onReferenceTypeSelected = formViewModel::setReferenceType,
                    onReferenceValueChange = formViewModel::setReference
                )

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}