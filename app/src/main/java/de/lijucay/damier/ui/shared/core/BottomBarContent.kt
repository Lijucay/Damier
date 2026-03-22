package de.lijucay.damier.ui.shared.core

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CookieButton(
    modifier: Modifier = Modifier,
    colors: IconButtonColors,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            modifier = Modifier
                .paddingWithSafeNavigationBar(16.dp)
                .size(80.dp),
            onClick = onClick,
            shape = MaterialShapes.Cookie12Sided.toShape(),
            colors = colors
        ) {
            Icon(
                imageVector = Icons.Rounded.Bolt,
                contentDescription = stringResource(R.string.check_in)
            )
        }
    }
}

@Composable
fun VersionInfo(modifier: Modifier = Modifier) {

}