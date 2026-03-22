package de.lijucay.damier.core.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import de.lijucay.damier.core.Preferences.Keys
import kotlinx.coroutines.launch

class UIViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _title = MutableStateFlow("Damier")
    val title = _title.asStateFlow()

    private val _detailsPage = MutableStateFlow<DetailsDestination>(DetailsDestination.ActivityDetails)
    val detailsPage = _detailsPage.asStateFlow()

    val showReference = dataStore.data
        .map { it[Keys.showReference] ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    val showMaxAmount = dataStore.data
        .map { it[Keys.showMaxAmount] ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    private val _isWidthAtLeastExpanded = MutableStateFlow(false)
    val isWidthAtLeastExpanded = _isWidthAtLeastExpanded.asStateFlow()

    private val _isHeightAtLeastExpanded = MutableStateFlow(false)
    val isHeightAtLeastExpanded = _isHeightAtLeastExpanded.asStateFlow()

    fun setDetailsPage(destination: DetailsDestination) {
        _detailsPage.value = destination
    }

    fun changeShowReference(shouldShow: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preference ->
                preference[Keys.showReference] = shouldShow
            }
        }
    }

    fun changeShowMaxAmount(shouldShow: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preference ->
                preference[Keys.showMaxAmount] = shouldShow
            }
        }
    }

    fun setWindowSizeInfo(isWidthAtLeastExpanded: Boolean, isHeightAtLeastExpanded: Boolean) {
        _isWidthAtLeastExpanded.value = isWidthAtLeastExpanded
        _isHeightAtLeastExpanded.value = isHeightAtLeastExpanded
    }
}