package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.ArrowNarrowRight
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.SmallText

fun LazyListScope.nfcChipItem(
    state: ActivityDetailsState,
    hasNfc: Boolean,
    onOpenNfcList: () -> Unit,
) {
    if (!hasNfc) return

    item {
        Card(
            modifier = Modifier
                .animateItem()
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            onClick = onOpenNfcList,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DefaultText(
                        text = stringResource(R.string.linked_tags),
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    Box(
                        modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        SmallText(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            text = state.allNfcChips.size.toString(),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        )
                    }
                    Icon(
                        imageVector = TablerIcons.ArrowNarrowRight,
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}