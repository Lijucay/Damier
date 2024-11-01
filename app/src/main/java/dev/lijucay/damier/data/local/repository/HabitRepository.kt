package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.model.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun loadHabits(): Flow<List<Habit>>
    suspend fun insertHabit(habit: Habit): Long
    suspend fun getHabitByTitle(title: String): Habit?
    fun getCurrentHabitByTitle(title: String): Flow<Habit?>
    suspend fun deleteHabit(title: String): Int
    suspend fun updateHabit(habit: Habit): Int
}