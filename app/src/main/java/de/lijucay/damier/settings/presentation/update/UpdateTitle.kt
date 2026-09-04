package de.lijucay.damier.settings.presentation.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.components.Badge
import de.lijucay.damier.core.presentation.components.TitleText

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
        Badge(
            backgroundColor = MaterialTheme.colorScheme.onPrimary,
            text = version,
            textColor = MaterialTheme.colorScheme.primary
        )
        TitleText(
            text = date,
            color = MaterialTheme.colorScheme.primary
        )
    }
}