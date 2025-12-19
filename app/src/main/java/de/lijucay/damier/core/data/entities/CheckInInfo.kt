package de.lijucay.damier.core.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ActivityInfo::class,
            parentColumns = ["id"],
            childColumns = ["activityId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["activityId"])]
)
data class CheckInInfo(
    @PrimaryKey(autoGenerate = false) val id: UUID = UUID.randomUUID(),
    val activityId: UUID,
    val timestamp: String,
    @ColumnInfo(defaultValue = "0") val checkInCount: Int
)
