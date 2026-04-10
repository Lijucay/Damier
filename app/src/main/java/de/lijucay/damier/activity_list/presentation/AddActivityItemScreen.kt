package de.lijucay.damier.activity_list.presentation

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.lijucay.damier.core.domain.ActivityFormMode
import de.lijucay.damier.core.presentation.screens.ActivityFormScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddActivityItemScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
) {
    ActivityFormScreen(
        modifier = modifier,
        mode = ActivityFormMode.Add,
        onNavigateBack = onNavigateBack
    )
}
