package dev.lijucay.damier.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.data.local.repository.HabitRepository
import dev.lijucay.damier.util.ResponseState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
): ViewModel() {
    private val _responseState = MutableStateFlow<ResponseState>(ResponseState.Loading)
    val responseState: StateFlow<ResponseState> = _responseState

    private val _habitList = MutableStateFlow<List<Habit>>(mutableListOf())
    val habitList: StateFlow<List<Habit>> = _habitList

    private val _currentSelectedHabit = MutableStateFlow<String?>(null)
    val currentSelectedHabit: StateFlow<String?> = _currentSelectedHabit

    fun loadHabits() {
        viewModelScope.launch {
            repository.loadHabits().collect { habits ->
                Log.d("HabitViewModel", "Habits were changed!")

                if (habits.isEmpty()) {
                    _responseState.update { ResponseState.Empty }
                } else {
                    Log.d("HabitViewModel", "The list is not empty. List: $habits")
                    _responseState.update { ResponseState.Success }
                    _habitList.update { habits }
                }
            }
        }
    }

    fun insertHabit(habit: Habit) = viewModelScope.launch { repository.insertHabit(habit) }

    fun updateHabit(habit: Habit) = viewModelScope.launch { repository.updateHabit(habit) }

    fun deleteHabit(title: String) = viewModelScope.launch { repository.deleteHabit(title) }

    fun setCurrentSelectedHabit(habitTitle: String?) = _currentSelectedHabit.update { habitTitle }
}