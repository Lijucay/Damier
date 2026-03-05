package de.lijucay.damier.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UIViewModel : ViewModel() {
    private val _detailsPage = MutableStateFlow<DetailsDestination?>(null)
    val detailsPage = _detailsPage.asStateFlow()

    fun setDetailsPage(destination: DetailsDestination) {
        _detailsPage.value = destination
    }
}