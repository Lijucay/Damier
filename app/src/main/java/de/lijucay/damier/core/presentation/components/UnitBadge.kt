package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import de.lijucay.damier.design.components.DefaultText

@Composable
fun UnitBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(shapes.extraLarge)
            .background(colorScheme.onSecondaryContainer)
            .padding(end = 4.dp)
    ) {
        DefaultText(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = text,
            color = colorScheme.secondaryContainer
        )
    }
}