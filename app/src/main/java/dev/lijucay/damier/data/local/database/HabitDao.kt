package dev.lijucay.damier.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.lijucay.damier.data.local.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("select * from Habit")
    fun getHabits(): Flow<List<Habit>>

    @Query("select * from Habit where id = :id")
    suspend fun getHabitById(id: Int): Habit?

    @Insert(Habit::class, OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: Habit): Long

    @Query("select * from Habit where id = :id")
    fun getCurrentHabitById(id: Int): Flow<Habit?>

    @Query("delete from Habit where id = :id")
    suspend fun deleteHabit(id: Int): Int

    @Update
    suspend fun updateHabit(habit: Habit): Int
}