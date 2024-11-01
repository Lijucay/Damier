package dev.lijucay.damier.data.local.repository

import dev.lijucay.damier.data.local.database.HabitDao
import dev.lijucay.damier.data.local.model.Habit
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitRepositoryImpl @Inject constructor(
    private val habitDao: HabitDao
) : HabitRepository {
    override fun loadHabits(): Flow<List<Habit>> {
        return habitDao.getHabits()
    }

    override suspend fun insertHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit)
    }

    override suspend fun getHabitByTitle(title: String): Habit? {
        return habitDao.getHabitByTitle(title)
    }

    override fun getCurrentHabitByTitle(title: String): Flow<Habit?> {
        return habitDao.getCurrentHabitByTitle(title)
    }

    override suspend fun deleteHabit(title: String): Int {
        return habitDao.deleteHabit(title)
    }

    override suspend fun updateHabit(habit: Habit): Int {
        return habitDao.updateHabit(habit)
    }
}