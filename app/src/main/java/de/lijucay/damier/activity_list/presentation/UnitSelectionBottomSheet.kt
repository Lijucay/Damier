package de.lijucay.damier.activity_list.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.units
import de.lijucay.damier.core.presentation.components.UnitRow
import de.lijucay.damier.core.presentation.getShortUnitNamesById
import de.lijucay.damier.core.presentation.toStringResource
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.shared.UnitId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UnitSelectionBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    selectedUnit: UnitId,
    onUnitSelected: (UnitId) -> Unit,
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    val groupedUnits = units.groupBy { it.group }
    val groups = groupedUnits.keys.toList()

    var selectedGroup by remember { mutableStateOf(groups.first()) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LargeTitleText(text = stringResource(R.string.select_unit))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
            ) {
                groups.forEachIndexed { index, group ->
                    TonalToggleButton(
                        checked = selectedGroup == group,
                        onCheckedChange = { selectedGroup = group },
                        modifier = Modifier
                            .semantics { role = Role.RadioButton },
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            groups.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        }
                    ) {
                        Text(text = stringResource(groups[index].toStringResource()))
                    }
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(groupedUnits[selectedGroup] ?: emptyList()) { unit ->
                    val unitName = unit.unitId.getShortUnitNamesById(context)

                    UnitRow(
                        label = stringResource(unit.unitId.toStringResource()),
                        badge = "${unitName.shortUnitSingular}/${unitName.shortUnitPlural}",
                        selected = selectedUnit == unit.unitId,
                        onClick = { onUnitSelected(unit.unitId) }
                    )
                }
            }
        }
    }
}