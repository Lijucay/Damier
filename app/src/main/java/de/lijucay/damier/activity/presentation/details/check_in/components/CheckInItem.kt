package de.lijucay.damier.activity.presentation.details.check_in.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.components.badge.Badge
import de.lijucay.damier.core.presentation.components.text.DefaultText
import de.lijucay.damier.core.presentation.components.ListCard
import de.lijucay.damier.check_in.presentation.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableTime

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

            Badge(
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                textColor = MaterialTheme.colorScheme.onTertiary,
                text = "${checkInUi.amount} $unitName"
            )
        }

        checkInUi.note?.let { NotesCard(it) }
    }
}