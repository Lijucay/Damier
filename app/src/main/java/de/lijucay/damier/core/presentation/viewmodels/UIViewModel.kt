package de.lijucay.damier.core.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.lijucay.damier.core.DataPreferences
import de.lijucay.damier.core.domain.DeletionMode
import de.lijucay.damier.core.domain.InfoMode
import de.lijucay.damier.core.presentation.DetailsDestination
import de.lijucay.damier.core.presentation.SnackbarEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class UIViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _title = MutableStateFlow("Damier")
    val title = _title.asStateFlow()

    private val _pendingActivityId = MutableStateFlow<UUID?>(null)
    val pendingActivityId = _pendingActivityId.asStateFlow()

    private val _detailsPage =
        MutableStateFlow<DetailsDestination>(DetailsDestination.ActivityDetails)
    val detailsPage = _detailsPage.asStateFlow()

    val showReference = dataStore.data
        .map { it[DataPreferences.Keys.showReference] ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    val showMaxAmount = dataStore.data
        .map { it[DataPreferences.Keys.showMaxAmount] ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    val savedDirUri = dataStore.data
        .map { it[DataPreferences.Keys.backupDirUri] }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    val firstLaunch = dataStore.data
        .map { it[DataPreferences.Keys.firstLaunch] ?: true }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = true
        )

    private val _isWidthAtLeastExpanded = MutableStateFlow(false)
    val isWidthAtLeastExpanded = _isWidthAtLeastExpanded.asStateFlow()

    private val _isHeightAtLeastExpanded = MutableStateFlow(false)
    val isHeightAtLeastExpanded = _isHeightAtLeastExpanded.asStateFlow()

    private val _deletionDialogMode = MutableStateFlow<DeletionMode?>(null)
    val deletionDialogMode = _deletionDialogMode.asStateFlow()

    private val _infoMode = MutableStateFlow<InfoMode?>(null)
    val infoMode = _infoMode.asStateFlow()

    private val _snackbarEvent = Channel<SnackbarEvent>(Channel.BUFFERED)
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    private val _showUpdateTimeline = MutableStateFlow(false)
    val showUpdateTimeline = _showUpdateTimeline.asStateFlow()

    fun emitSnackbar(event: SnackbarEvent) {
        viewModelScope.launch { _snackbarEvent.send(event) }
    }

    fun setDetailsPage(destination: DetailsDestination) {
        _detailsPage.value = destination
    }

    fun changeShowReference(shouldShow: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preference ->
                preference[DataPreferences.Keys.showReference] = shouldShow
            }
        }
    }

    fun changeShowMaxAmount(shouldShow: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preference ->
                preference[DataPreferences.Keys.showMaxAmount] = shouldShow
            }
        }
    }

    fun setSavedDirUri(uri: String?) {
        viewModelScope.launch {
            dataStore.edit { preference ->
                if (uri == null) {
                    preference.remove(DataPreferences.Keys.backupDirUri)
                } else {
                    preference[DataPreferences.Keys.backupDirUri] = uri
                }
            }
        }
    }

    fun setWindowSizeInfo(isWidthAtLeastExpanded: Boolean, isHeightAtLeastExpanded: Boolean) {
        _isWidthAtLeastExpanded.value = isWidthAtLeastExpanded
        _isHeightAtLeastExpanded.value = isHeightAtLeastExpanded
    }

    fun setDeletionMode(deletionMode: DeletionMode?) {
        _deletionDialogMode.value = deletionMode
    }

    fun setInfoMode(infoMode: InfoMode?) {
        _infoMode.value = infoMode
    }

    fun setFirstLaunch(firstLaunch: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[DataPreferences.Keys.firstLaunch] = firstLaunch
            }
        }
    }

    fun setShowUpdateTimeline(show: Boolean) {
        _showUpdateTimeline.value = show
    }

    fun setPendingActivityId(id: UUID?) {
        _pendingActivityId.value = id
    }
}
