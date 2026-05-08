package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableTime
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.ListCard

@Composable
fun CheckInItem(
    modifier: Modifier = Modifier,
    checkInUi: CheckInUi,
    unitName: String,
    isItemFirst: Boolean = false,
    isItemLast: Boolean = false,
    onClick: () -> Unit
) {

    ListCard(
        modifier = modifier,
        isItemFirst = isItemFirst,
        isItemLast = isItemLast,
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DefaultText(
                modifier = Modifier.weight(1f),
                text = checkInUi.dateTime.value.toLocalTime()
                    .toDisplayableTime().formatted
            )

            Box(
                Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                DefaultText(
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "${checkInUi.amount} $unitName"
                )
            }
        }
    }
}