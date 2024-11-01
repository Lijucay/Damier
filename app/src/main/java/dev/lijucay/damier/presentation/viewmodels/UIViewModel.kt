package dev.lijucay.damier.presentation.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lijucay.damier.util.ExportUtil
import dev.lijucay.damier.util.ImportUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UIViewModel @Inject constructor(
    private val exportUtil: ExportUtil,
    private val importUtil: ImportUtil
): ViewModel() {
    private val _currentTitle = MutableStateFlow<String?>(null)
    val currentTitle: StateFlow<String?> = _currentTitle

    private val _showCounterDialog = MutableStateFlow(false)
    val showCounterDialog: StateFlow<Boolean> = _showCounterDialog

    private val _showAddHabitDialog = MutableStateFlow(false)
    val showAddHabitDialog: StateFlow<Boolean> = _showAddHabitDialog

    private val _infoDialogTitle = MutableStateFlow("")
    val infoDialogTitle: StateFlow<String> = _infoDialogTitle

    private val _infoDialogMessage = MutableStateFlow("")
    val infoDialogMessage: StateFlow<String> = _infoDialogMessage

    private val _showInfoDialog = MutableStateFlow(false)
    val showInfoDialog: StateFlow<Boolean> = _showInfoDialog

    private val _showEditHabitDialog = MutableStateFlow(false)
    val showEditHabitDialog: StateFlow<Boolean> = _showEditHabitDialog

    private val _showEditDefaultGoalDialog = MutableStateFlow(false)
    val showEditDefaultGoalDialog: StateFlow<Boolean> = _showEditDefaultGoalDialog

    private val _currentFileUri = MutableStateFlow<Uri?>(null)
    val currentFileUri: StateFlow<Uri?> = _currentFileUri

    private val _showImportDialog = MutableStateFlow(false)
    val showImportDialog: StateFlow<Boolean> = _showImportDialog

    private val _showCheckIns = MutableStateFlow(false)
    val showCheckIns: StateFlow<Boolean> = _showCheckIns

    fun setShowImportDialog(show: Boolean) = _showImportDialog.update { show }

    fun setCurrentTitle(title: String?) = _currentTitle.update { title }

    fun setShowCounterDialog(show: Boolean) = _showCounterDialog.update { show }

    fun setShowAddHabitDialog(show: Boolean) = _showAddHabitDialog.update { show }

    fun setShowEditHabitDialog(show: Boolean) = _showEditHabitDialog.update { show }

    fun setShowEditDefaultGoalDialog(show: Boolean) = _showEditDefaultGoalDialog.update { show }

    fun setInfoDialogInfo(title: String = "", message: String = "", show: Boolean) {
        _infoDialogTitle.update { title }
        _infoDialogMessage.update { message }
        _showInfoDialog.update { show }
    }

    fun setCurrentFileUri(uri: Uri?) = _currentFileUri.update { uri }

    fun exportData(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            onComplete(exportUtil.exportData())
        }
    }

    fun importData(
        fileUri: Uri,
        onTotalCountUpdate: (Int) -> Unit,
        onCurrentCountUpdate: (Int) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            importUtil.importData(
                fileUri,
                onTotalCountUpdate,
                onCurrentCountUpdate,
                onComplete
            )
        }
    }

    fun setShowCheckIns(show: Boolean) {
        _showCheckIns.update { show }
    }
}