package de.lijucay.damier.core.presentation.components.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowRight
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.UnitBadge
import de.lijucay.damier.core.presentation.getLongUnitNamesById
import de.lijucay.damier.core.presentation.getShortUnitNamesById
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
                    imageVector = TablerIcons.ArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}