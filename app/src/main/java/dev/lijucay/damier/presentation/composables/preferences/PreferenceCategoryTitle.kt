package dev.lijucay.damier.presentation.composables.preferences

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceCategoryTitle(
    title: String
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}