package de.lijucay.damier.settings.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.BuildConfig
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.UIViewModel
import de.lijucay.damier.ui.shared.core.ScreenContainer
import de.lijucay.damier.settings.presentation.components.SwitchPreference
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    val uiViewModel = koinViewModel<UIViewModel>()
    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    val appVersion = BuildConfig.VERSION_NAME

    val showReference by uiViewModel.showReference.collectAsStateWithLifecycle()
    val showMaxAmount by uiViewModel.showMaxAmount.collectAsStateWithLifecycle()

    ScreenContainer(
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.settings),
        bottomBarContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.version, appVersion))
                Text(text = stringResource(R.string.credits))
            }
        },
        navigationIcon = {
            if (!isWidthAtLeastExpanded) {
                IconButton(
                    onClick = onNavigateBack
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(R.string.activity_options),
                style = typography.titleMedium.copy(color = colorScheme.primary)
            )
            SwitchPreference(
                title = stringResource(R.string.show_reference),
                subTitle = stringResource(id = R.string.show_reference_explanation),
                checked = showReference,
                icon = ImageVector.vectorResource(R.drawable.rounded_counter_ref)
            ) { checked ->
                uiViewModel.changeShowReference(checked)
            }
            AnimatedVisibility(
                visible = showReference
            ) {
                SwitchPreference(
                    title = stringResource(R.string.show_max),
                    subTitle = stringResource(R.string.show_max_explanation),
                    checked = showMaxAmount,
                    icon = ImageVector.vectorResource(R.drawable.rounded_counter_max)
                ) { checked ->
                    uiViewModel.changeShowMaxAmount(checked)
                }
            }
        }
    }
}