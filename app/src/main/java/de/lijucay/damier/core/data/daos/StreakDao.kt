package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import de.lijucay.damier.core.data.entities.Streak
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface StreakDao {
    @Query("SELECT * FROM streak WHERE activityId = :activityId ORDER BY endDate DESC LIMIT 1")
    suspend fun getLatest(activityId: UUID): Streak?

    @Query("SELECT * FROM streak WHERE activityId = :activityId ORDER BY endDate DESC LIMIT 2")
    fun getLastTwo(activityId: UUID): Flow<List<Streak>>

    @Query("SELECT MAX(length) FROM streak WHERE activityId = :activityId")
    fun getLongest(activityId: UUID): Flow<Int?>

    @Query("SELECT * FROM streak")
    suspend fun getAllStreaks(): List<Streak>

    @Upsert
    suspend fun upsert(streak: Streak)

    @Upsert
    suspend fun upsertAll(streaks: List<Streak>)

    @Update
    suspend fun update(streak: Streak)

    @Delete
    suspend fun delete(streak: Streak)

    @Query("DELETE FROM streak WHERE activityId = :activityId")
    suspend fun deleteAllForActivity(activityId: UUID)

    @Transaction
    suspend fun replaceAllForActivity(activityId: UUID, newStreaks: List<Streak>) {
        deleteAllForActivity(activityId)

        if (newStreaks.isNotEmpty()) upsertAll(newStreaks)
    }
}