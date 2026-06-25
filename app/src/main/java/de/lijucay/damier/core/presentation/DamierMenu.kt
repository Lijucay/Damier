package de.lijucay.damier.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import compose.icons.TablerIcons
import compose.icons.tablericons.DotsVertical
import de.lijucay.damier.R

@Composable
fun DamierMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onShowMenu: (Boolean) -> Unit,
    menuItems: @Composable (ColumnScope.() -> Unit)
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = { onShowMenu(true) }
        ) {
            Icon(
                imageVector = TablerIcons.DotsVertical,
                contentDescription = stringResource(R.string.menu)
            )
        }

        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = { onShowMenu(false) },
            shape = MaterialTheme.shapes.extraLarge,
            content = menuItems
        )
    }
}