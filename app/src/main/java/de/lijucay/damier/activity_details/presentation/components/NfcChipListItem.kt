package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Minus
import compose.icons.tablericons.Pencil
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.Badge
import de.lijucay.damier.core.presentation.components.LargeTitleText
import de.lijucay.damier.core.presentation.models.NfcChipUi
import de.lijucay.damier.core.presentation.models.toDisplayableDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun NfcChipListItem(
    modifier: Modifier = Modifier,
    nfcChip: NfcChipUi,
    onEditLabelClicked: (UUID, String) -> Unit,
    onUnlinkClicked: () -> Unit
) {
    val untitled = stringResource(R.string.untitled)
    val currentLocale = LocalLocale.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        ),
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
                LargeTitleText(text = nfcChip.label ?: untitled)
                Badge(
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    text = nfcChip.linkedAt.toDisplayableDateTime(
                        formatter = DateTimeFormatter.ofPattern(
                            "d MMMM yyyy, HH:mm:ss",
                            currentLocale.platformLocale
                        )
                    ).formatted
                )
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