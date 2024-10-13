package dev.lijucay.damier.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UIViewModel : ViewModel() {
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
}