package dev.lijucay.damier.data.local.database

import androidx.room.Dao
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

    @Query("select * from Habit")
    fun getCurrentHabits():List<Habit>

    @Query("select * from Habit where title = :title")
    suspend fun getHabitByTitle(title: String): Habit?

    @Insert(Habit::class, OnConflictStrategy.IGNORE)
    suspend fun insertHabit(habit: Habit): Long

    @Query("select * from Habit where title = :title")
    fun getCurrentHabitByTitle(title: String): Flow<Habit?>

    @Query("delete from Habit where title = :title")
    suspend fun deleteHabit(title: String): Int

    @Update
    suspend fun updateHabit(habit: Habit): Int
}