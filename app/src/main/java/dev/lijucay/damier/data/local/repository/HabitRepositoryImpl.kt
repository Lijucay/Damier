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

    override suspend fun getHabitByID(id: Int): Habit? {
        return habitDao.getHabitById(id)
    }

    override fun getCurrentHabitByID(id: Int): Flow<Habit?> {
        return habitDao.getCurrentHabitById(id)
    }

    override suspend fun deleteHabit(id: Int): Int {
        return habitDao.deleteHabit(id)
    }

    override suspend fun updateHabit(habit: Habit): Int {
        return habitDao.updateHabit(habit)
    }
}