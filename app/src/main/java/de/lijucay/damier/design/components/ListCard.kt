package de.lijucay.damier.design.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    isItemFirst: Boolean = false,
    isItemLast: Boolean = false,
    cardColors: CardColors = CardDefaults.cardColors(),
    onClick: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit)
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
        colors = cardColors,
        content = content
    )
}