package de.lijucay.damier.settings.presentation.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import de.lijucay.damier.design.components.TitleText

@Composable
fun UpdateTitle(
    modifier: Modifier = Modifier,
    version: String,
    date: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = version,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        TitleText(
            text = date,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}