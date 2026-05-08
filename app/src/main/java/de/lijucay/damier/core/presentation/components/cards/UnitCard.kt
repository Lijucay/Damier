package de.lijucay.damier.core.presentation.components.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.domain.getShortUnitNamesById
import de.lijucay.damier.core.domain.units
import de.lijucay.damier.core.presentation.models.ActivityFormState
import de.lijucay.damier.core.presentation.components.ExpandableLayout
import de.lijucay.damier.core.presentation.components.UnitBadge
import de.lijucay.damier.core.presentation.components.UnitRow
import de.lijucay.damier.core.presentation.components.SwitchPreference
import de.lijucay.damier.design.components.TitleText

@Composable
fun UnitCard(
    state: ActivityFormState,
    onUseUnitsToggle: () -> Unit,
    onShowUnitsToggle: () -> Unit,
    onUnitSelected: (UnitId) -> Unit
) {
    val context = LocalContext.current

    ConditionalOptionsCard(
        showOption = state.useUnits,
        parentLayout = {
            SwitchPreference(
                checked = state.useUnits,
                title = stringResource(R.string.use_units),
                subTitle = stringResource(R.string.use_units_explanation),
                columnPadding = PaddingValues(16.dp),
                onCheckedChange = { onUseUnitsToggle() }
            )
        },
        optionLayout = {
            ExpandableLayout(
                title = stringResource(R.string.unit),
                expanded = state.showUnits,
                arrowContent = {
                    UnitBadge(
                        text = "${
                            state.unitId.getLongUnitNamesById(context).singularName
                        }/${
                            state.unitId.getLongUnitNamesById(context).pluralName
                        }"
                    )
                }
            ) {
                Column {
                    val groupedUnits = units.groupBy { it.group }
                    groupedUnits.forEach { (group, units) ->
                        TitleText(text = group.name.lowercase().replaceFirstChar { it.uppercase() })
                        units.forEach { unit ->
                            UnitRow(
                                label = unit.unitId.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                badge = "${
                                    unit.unitId.getShortUnitNamesById(context).shortUnitSingular
                                }/${
                                    unit.unitId.getShortUnitNamesById(context).shortUnitPlural
                                }",
                                selected = state.unitId == unit.unitId,
                                onClick = { onUnitSelected(unit.unitId) }
                            )
                        }
                    }
                }
            }
        },
        optionPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
        onParentClicked = onUseUnitsToggle,
        onOptionClicked = onShowUnitsToggle
    )
}