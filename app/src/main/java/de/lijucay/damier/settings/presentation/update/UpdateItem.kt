package de.lijucay.damier.settings.presentation.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.BuildConfig

@Composable
fun UpdateItem(
    modifier: Modifier = Modifier,
    highlightUpdate: HighlightUpdate? = null,
    version: String,
    date: String,
    releaseNotes: List<String>
) {
    Card(
        colors = if (version == BuildConfig.VERSION_NAME) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else CardDefaults.cardColors(),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            UpdateTitle(
                version = version,
                date = date
            )

            highlightUpdate?.let { highlightUpdate ->
                HighlightUpdateCard(
                    title = highlightUpdate.title,
                    text = highlightUpdate.text,
                    icon = highlightUpdate.icon
                )
            }

            releaseNotes.forEach { releaseNote ->
                HorizontalDivider(
                    color = if (version == BuildConfig.VERSION_NAME) MaterialTheme.colorScheme.onPrimaryContainer
                    else DividerDefaults.color
                )
                UpdateNote(note = releaseNote)
            }
        }
    }
}