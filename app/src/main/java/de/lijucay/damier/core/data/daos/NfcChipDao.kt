package de.lijucay.damier.core.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.lijucay.damier.core.data.entities.NfcChipInfo
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface NfcChipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chip: NfcChipInfo)

    @Query("SELECT activityId FROM NfcChipInfo WHERE chipId = :chipId")
    suspend fun getActivityIdForChip(chipId: UUID): UUID?

    @Query("SELECT * FROM NfcChipInfo WHERE chipId = :chipId")
    suspend fun getByChipId(chipId: UUID): NfcChipInfo?

    @Query("DELETE FROM NfcChipInfo WHERE chipId = :chipId")
    suspend fun deleteByChipId(chipId: UUID)

    @Query("SELECT * FROM NfcChipInfo WHERE activityId = :activityId ORDER BY linkedAt DESC")
    fun getChipsForActivity(activityId: UUID): Flow<List<NfcChipInfo>>

    @Query("SELECT COUNT(*) FROM NfcChipInfo WHERE activityId = :activityId")
    suspend fun countForActivity(activityId: UUID): Int

    @Query("SELECT * FROM NfcChipInfo")
    suspend fun getAllChips(): List<NfcChipInfo>

    @Query("UPDATE NfcChipInfo SET label = :label WHERE chipId = :chipId")
    suspend fun updateLabel(label: String, chipId: UUID)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(chips: List<NfcChipInfo>)
}