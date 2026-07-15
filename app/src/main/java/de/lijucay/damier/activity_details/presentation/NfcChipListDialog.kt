package de.lijucay.damier.activity_details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.Plus
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.components.NfcChipListItem
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.models.NfcChipUi
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.LargeTitleText
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcChipListDialog(
    sheetState: SheetState,
    nfcChips: List<NfcChipUi>,
//    useLimitTheme: Boolean,
    onEditLabelClicked: (UUID, String) -> Unit,
    onUnlinkRequest: (UUID) -> Unit,
    onLinkRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LargeTitleText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        color = AlertDialogDefaults.titleContentColor,
                        text = stringResource(R.string.linked_nfc_chips)
                    )

                    IconButton(onClick = onLinkRequest) {
                        Icon(
                            imageVector = TablerIcons.Plus,
                            contentDescription = stringResource(R.string.link_nfc_tag)
                        )
                    }
                }
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = bottomPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        LargeText(
                            text = stringResource(R.string.unlink_chip_warning)
                        )
                    }
                }
            }

            items(nfcChips) { chip ->
                NfcChipListItem(
                    modifier = Modifier
                        .animateItem()
                        .padding(horizontal = 16.dp),
                    onEditLabelClicked = onEditLabelClicked,
                    nfcChip = chip
                ) {
                    onUnlinkRequest(chip.id)
                }
            }
        }
    }
}
