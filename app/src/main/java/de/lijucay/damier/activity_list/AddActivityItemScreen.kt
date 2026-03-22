package de.lijucay.damier.activity_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.core.data.entities.ActivityInfo
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.domain.getShortUnitNamesById
import de.lijucay.damier.core.domain.getUnits
import de.lijucay.damier.core.presentation.UIViewModel
import de.lijucay.damier.ui.shared.core.ConditionalOptionsCard
import de.lijucay.damier.ui.shared.core.ExpandableLayout
import de.lijucay.damier.ui.shared.core.ScreenContainer
import de.lijucay.damier.ui.shared.core.WaffleDiagram
import de.lijucay.damier.settings.presentation.components.SwitchPreference
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddActivityItemScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    val uiViewModel = koinViewModel<UIViewModel>()
    val activityListViewModel = koinViewModel<ActivityListViewModel>()

    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val activityId = UUID.randomUUID()
    val primaryColor = colorScheme.primary

    val scrollState = rememberScrollState()

    var title by remember { mutableStateOf("") }
    var unitId by remember { mutableStateOf(UnitId.TIMES) }
    var reference by remember { mutableStateOf("1") }
    var referenceType by remember { mutableStateOf(ReferenceType.GOAL) }
    var defaultAmount by remember { mutableStateOf("1") }

    var useUnits by remember { mutableStateOf(false) }
    var showUnits by remember { mutableStateOf(false) }

    var useDefaultAmount by remember { mutableStateOf(false) }

    var useReference by remember { mutableStateOf(false) }
    var showReferenceTypes by remember { mutableStateOf(false) }

    val isSaveEnabled by remember {
        derivedStateOf {
            defaultAmount.toIntOrNull() != null
                    && title.isNotBlank()
                    && reference.toIntOrNull() != null
                    && reference.toInt() > 0
        }
    }

    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.add_activity),
        topAppBarActions = {
            FilledIconButton(
                enabled = isSaveEnabled,
                onClick = {
                    val activity = ActivityInfo(
                        id = activityId,
                        activityName = title,
                        unit = if (useUnits) unitId else UnitId.TIMES,
                        reference = reference.toInt(),
                        referenceType = if (useReference) referenceType else ReferenceType.MAX,
                        defaultAmount = if (useDefaultAmount) defaultAmount.toInt() else 1
                    )

                    activityListViewModel.insertActivity(activity)

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
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.primaryContainer,
                    contentColor = colorScheme.onPrimaryContainer
                ),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WaffleDiagram(
                        reference = 10,
                        type = ReferenceType.GOAL,
                        checkIns = listOf()
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = colorScheme.secondaryContainer,
                    unfocusedContainerColor = colorScheme.secondaryContainer
                ),
                placeholder = {
                    Text(text = stringResource(R.string.title))
                },
                value = title,
                onValueChange = { title = it },
                shape = shapes.extraLargeIncreased,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(R.string.tracking_options),
                style = typography.titleLargeEmphasized.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(8.dp))

            ConditionalOptionsCard(
                showOption = useUnits,
                parentLayout = {
                    SwitchPreference(
                        checked = useUnits,
                        title = stringResource(R.string.use_units),
                        subTitle = stringResource(R.string.use_units_explanation),
                        columnPadding = PaddingValues(16.dp),
                        onCheckedChange = { useUnits = it }
                    )
                },
                optionLayout = {
                    ExpandableLayout(
                        title = stringResource(R.string.unit),
                        expanded = showUnits,
                        arrowContent = {
                            Box(
                                modifier = Modifier
                                    .clip(shapes.extraLarge)
                                    .background(colorScheme.primaryContainer)
                                    .padding(end = 4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = "${
                                        unitId.getLongUnitNamesById(context).singularName
                                    }/${
                                        unitId.getLongUnitNamesById(context).pluralName
                                    }",
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    ) {
                        Column {
                            val groupedUnits = getUnits(context).groupBy { it.group }

                            groupedUnits.forEach { (group, units) ->
                                Column {
                                    Text(
                                        text = group.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )

                                    units.forEach { unit ->
                                        Box(
                                            modifier = Modifier
                                                .clip(shapes.extraLarge)
                                                .clickable(
                                                    onClick = { unitId = unit.unitId },
                                                    role = Role.RadioButton
                                                )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                RadioButton(
                                                    selected = unitId == unit.unitId,
                                                    onClick = null
                                                )
                                                Text(
                                                    unit
                                                        .unitId
                                                        .name
                                                        .lowercase()
                                                        .replaceFirstChar { it.uppercase() }
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .clip(shapes.extraLarge)
                                                        .background(colorScheme.primaryContainer)
                                                        .padding(end = 4.dp)
                                                ) {
                                                    Text(
                                                        modifier = Modifier.padding(horizontal = 8.dp),
                                                        text = "${
                                                            unit
                                                                .unitId
                                                                .getShortUnitNamesById(context)
                                                                .shortUnitSingular
                                                        }/${
                                                            unit
                                                                .unitId
                                                                .getShortUnitNamesById(context)
                                                                .shortUnitPlural
                                                        }",
                                                        color = colorScheme.onPrimaryContainer
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                optionPadding = PaddingValues(
                    vertical = 20.dp,
                    horizontal = 16.dp
                ),
                onParentClicked = {
                    useUnits = !useUnits
                },
                onOptionClicked = {
                    showUnits = !showUnits
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = useUnits) {
                ConditionalOptionsCard(
                    showOption = useDefaultAmount,
                    parentLayout = {
                        SwitchPreference(
                            checked = useDefaultAmount,
                            title = stringResource(R.string.use_default_amount),
                            subTitle = stringResource(R.string.use_default_amount_explanation),
                            columnPadding = PaddingValues(16.dp),
                            onCheckedChange = { useDefaultAmount = it }
                        )
                    },
                    optionLayout = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.default_amount),
                                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )

                            BasicTextField(
                                modifier = Modifier.width(32.dp),
                                value = defaultAmount,
                                onValueChange = { amount ->
                                    defaultAmount = amount
                                },
                                cursorBrush = SolidColor(primaryColor),
                                textStyle = typography.bodyLarge.copy(
                                    color = primaryColor,
                                    fontWeight = FontWeight.Bold
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                )
                            )
                        }
                    },
                    optionPadding = PaddingValues(
                        vertical = 20.dp,
                        horizontal = 16.dp
                    ),
                    onParentClicked = { useDefaultAmount = !useDefaultAmount },
                    onOptionClicked = { /* TODO: Focus text field */ }
                )
            }

            Spacer(Modifier.height(8.dp))

            ConditionalOptionsCard(
                showOption = useReference,
                parentLayout = {
                    SwitchPreference(
                        checked = useReference,
                        title = stringResource(R.string.override_reference_type),
                        subTitle = stringResource(R.string.override_reference_type_explanation),
                        columnPadding = PaddingValues(16.dp),
                        onCheckedChange = { useReference = it }
                    )
                },
                optionLayout = {
                    ExpandableLayout(
                        title = stringResource(R.string.reference_type),
                        expanded = showReferenceTypes,
                        arrowContent = {
                            Box(
                                modifier = Modifier
                                    .clip(shapes.extraLarge)
                                    .background(colorScheme.primaryContainer)
                                    .padding(end = 4.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    text = "${
                                        referenceType.name.lowercase().replaceFirstChar {
                                            it.uppercase()
                                        }
                                    }: $reference",
                                    color = colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    ) {
                        Column {
                            ReferenceType.entries.forEach { type ->
                                if (type != ReferenceType.MAX) {
                                    Box(
                                        modifier = Modifier
                                            .clip(shapes.extraLarge)
                                            .clickable(
                                                onClick = { referenceType = type },
                                                role = Role.RadioButton
                                            )
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                RadioButton(
                                                    selected = type == referenceType,
                                                    onClick = null
                                                )
                                                Text(type.name.lowercase().replaceFirstChar { it.uppercase() })
                                            }
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${
                                        referenceType
                                            .name
                                            .lowercase()
                                            .replaceFirstChar { 
                                                it.uppercase()
                                            }
                                    }:"
                                )
                                BasicTextField(
                                    modifier = Modifier.width(32.dp),
                                    value = reference,
                                    onValueChange = { newRef ->
                                        reference = newRef
                                    },
                                    cursorBrush = SolidColor(primaryColor),
                                    textStyle = typography.bodyLarge.copy(
                                        color = primaryColor,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    )
                                )
                            }
                        }
                    }
                },
                onParentClicked = { useReference = !useReference },
                onOptionClicked = { showReferenceTypes = !showReferenceTypes }
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}