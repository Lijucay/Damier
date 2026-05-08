package de.lijucay.damier.core.presentation.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText

@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    subTitle: String?,
    checked: Boolean,
    columnPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    onCheckedChange: (Boolean) -> Unit
) {
    Box(modifier = modifier.clip(RoundedCornerShape(24.dp))) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = checked,
                    role = Role.Switch,
                    onValueChange = onCheckedChange
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 16.dp,
                            end = 8.dp,
                            bottom = 8.dp
                        )
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(columnPadding),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                TitleText(text = title)

                subTitle?.let { t ->
                    Spacer(modifier = Modifier.height(2.dp))
                    SmallText(text = t)
                }
            }

            Switch(
                modifier = Modifier.padding(end = 16.dp),
                checked = checked,
                onCheckedChange = null,
                thumbContent = {
                    Crossfade(
                        targetState = checked
                    ) { isChecked ->
                        Icon(
                            imageVector = if (isChecked)
                                Icons.Rounded.Check
                            else
                                Icons.Rounded.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }

}