package de.lijucay.damier.core.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import de.lijucay.damier.core.data.converter.LocalDateTimeSerializer
import de.lijucay.damier.core.data.converter.UUIDSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
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
@TypeConverters(LocalDateTimeConverter::class)
data class CheckInInfo(
    @PrimaryKey(autoGenerate = false)
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    val activityId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    val timestamp: LocalDateTime,
    @SerialName("checkInCount")
    @ColumnInfo(defaultValue = "0") val amount: Int
)
