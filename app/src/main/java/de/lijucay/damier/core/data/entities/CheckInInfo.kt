package de.lijucay.damier.core.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import java.time.LocalDateTime
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
@TypeConverters(LocalDateTimeConverter::class)
data class CheckInInfo(
    @PrimaryKey(autoGenerate = false) val id: UUID = UUID.randomUUID(),
    val activityId: UUID,
    val timestamp: LocalDateTime,
    @SerializedName("checkInCount", alternate = ["amount"])
    @ColumnInfo(defaultValue = "0") val amount: Int
)
