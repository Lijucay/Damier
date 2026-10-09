package de.lijucay.damier.activity.presentation.details.check_in.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.presentation.components.text.DefaultText

@Composable
fun NotesCard(note: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        DefaultText(
            text = note,
            modifier = Modifier.fillMaxWidth()
        )
    }
}