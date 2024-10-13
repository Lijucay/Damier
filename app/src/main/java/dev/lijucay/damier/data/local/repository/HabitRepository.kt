package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun loadHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun getHabitByID(id: Int): Habit?
    fun getCurrentHabitByID(id: Int): Flow<Habit?>
    suspend fun deleteHabit(id: Int): Int
    suspend fun updateHabit(habit: Habit): Int
}