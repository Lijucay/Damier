package de.lijucay.damier.core.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.domain.getShortUnitNamesById
import de.lijucay.damier.core.presentation.components.UnitBadge
import de.lijucay.damier.core.presentation.models.ActivityFormState
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText

@Composable
fun UnitCard(
    state: ActivityFormState,
    onClick: () -> Unit,
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleText(text = stringResource(R.string.unit))
                SmallText(
                    text = state.unitId.getLongUnitNamesById(context).singularName
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UnitBadge(
                    text = "${state.unitId.getShortUnitNamesById(context).shortUnitSingular}/${state.unitId.getShortUnitNamesById(context).shortUnitPlural}"
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }


//    ConditionalOptionsCard(
//        showOption = state.useUnits,
//        parentLayout = {
//            SwitchPreference(
//                checked = state.useUnits,
//                title = stringResource(R.string.use_units),
//                subTitle = stringResource(R.string.use_units_explanation),
//                columnPadding = PaddingValues(16.dp),
//                onCheckedChange = { onUseUnitsToggle() }
//            )
//        },
//        optionLayout = {
//            ExpandableLayout(
//                title = stringResource(R.string.unit),
//                expanded = state.showUnits,
//                arrowContent = {
//                    UnitBadge(
//                        text = "${
//                            state.unitId.getLongUnitNamesById(context).singularName
//                        }/${
//                            state.unitId.getLongUnitNamesById(context).pluralName
//                        }"
//                    )
//                }
//            ) {
//                Column {
//                    val groupedUnits = units.groupBy { it.group }
//                    groupedUnits.forEach { (group, units) ->
//                        TitleText(text = group.name.lowercase().replaceFirstChar { it.uppercase() })
//                        units.forEach { unit ->
//                            UnitRow(
//                                label = unit.unitId.name.lowercase()
//                                    .replaceFirstChar { it.uppercase() },
//                                badge = "${
//                                    unit.unitId.getShortUnitNamesById(context).shortUnitSingular
//                                }/${
//                                    unit.unitId.getShortUnitNamesById(context).shortUnitPlural
//                                }",
//                                selected = state.unitId == unit.unitId,
//                                onClick = { onUnitSelected(unit.unitId) }
//                            )
//                        }
//                    }
//                }
//            }
//        },
//        optionPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
//        onParentClicked = onUseUnitsToggle,
//        onOptionClicked = onShowUnitsToggle
//    )
}