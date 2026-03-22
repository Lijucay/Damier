package de.lijucay.damier.core.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateConverter
import java.time.LocalDate
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
@TypeConverters(LocalDateConverter::class)
data class Streak(
    @PrimaryKey(autoGenerate = false) val id: UUID = UUID.randomUUID(),
    val activityId: UUID,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val length: Int
)
