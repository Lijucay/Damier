package de.lijucay.damier.core.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateConverter
import de.lijucay.damier.core.data.converter.LocalDateSerializer
import de.lijucay.damier.core.data.converter.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
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
    @PrimaryKey(autoGenerate = false)
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    val activityId: UUID,
    @Serializable(with = LocalDateSerializer::class)
    val startDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val endDate: LocalDate,
    val length: Int
)
