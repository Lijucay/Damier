package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableTime

@Composable
fun CheckInItem(
    modifier: Modifier = Modifier,
    checkInUi: CheckInUi,
    unitName: String,
    isItemFirst: Boolean = false,
    isItemLast: Boolean = false,
    usesLimitColors: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(
            topStart = if (isItemFirst) 28.dp else 4.dp,
            topEnd = if (isItemFirst) 28.dp else 4.dp,
            bottomEnd = if (isItemLast) 28.dp else 4.dp,
            bottomStart = if (isItemLast) 28.dp else 4.dp
        ),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (usesLimitColors)
                colorScheme.errorContainer
            else
                colorScheme.primaryContainer,
            contentColor = if (usesLimitColors)
                colorScheme.onErrorContainer
            else
                colorScheme.onPrimaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = checkInUi.dateTime.value.toLocalTime()
                    .toDisplayableTime().formatted
            )

            Box(
                Modifier
                    .clip(shape = shapes.extraLarge)
                    .background(
                        if (usesLimitColors)
                            colorScheme.error
                        else
                            colorScheme.tertiaryContainer
                    )
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = if (usesLimitColors)
                        colorScheme.onError
                    else
                        colorScheme.onTertiaryContainer,
                    text = "${checkInUi.amount} $unitName"
                )
            }
        }
    }
}