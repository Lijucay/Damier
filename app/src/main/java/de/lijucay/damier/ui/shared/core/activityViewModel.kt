package de.lijucay.damier.ui.shared.core

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
inline fun <reified T: ViewModel> activityViewModel(): T {
    return koinViewModel<T>(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
}