package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import compose.icons.tablericons.Pencil
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.models.NfcChipUi
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText
import java.util.UUID

@Composable
fun NfcChipListItem(
    modifier: Modifier = Modifier,
    nfcChip: NfcChipUi,
    onEditLabelClicked: (UUID, String) -> Unit,
    onUnlinkClicked: () -> Unit
) {
    val untitled = stringResource(R.string.untitled)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TitleText(text = nfcChip.label ?: untitled)
                Box(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.extraLarge)
                        .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    SmallText(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        text = nfcChip.linkedAt.toDisplayableDateTime().formatted,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEditLabelClicked(nfcChip.id, nfcChip.label ?: untitled) }) {
                    Icon(
                        imageVector = TablerIcons.Pencil,
                        contentDescription = stringResource(R.string.unlink)
                    )
                }
                IconButton(onClick = onUnlinkClicked) {
                    Icon(
                        imageVector = TablerIcons.Minus,
                        contentDescription = stringResource(R.string.unlink)
                    )
                }
            }
        }
    }
}