package dev.lijucay.damier.presentation.composables.preferences

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchPreference(
    title: String,
    summary: String,
    checked: Boolean,
    iconVector: ImageVector,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .selectable(
                    selected = checked,
                    role = Role.Switch,
                    onClick = { onCheckedChange(!checked) }
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = null,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .animateContentSize()
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = summary,
                    fontSize = 14.sp,
                )
            }
            Switch(
                modifier = Modifier.padding(end = 16.dp),
                checked = checked,
                onCheckedChange = null
            )
        }
    }
}