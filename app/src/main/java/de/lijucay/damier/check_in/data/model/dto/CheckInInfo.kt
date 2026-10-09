package de.lijucay.damier.check_in.data.model.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import de.lijucay.damier.core.data.converter.LocalDateTimeSerializer
import de.lijucay.damier.core.data.converter.UUIDSerializer
import de.lijucay.damier.activity.data.model.dto.ActivityInfo
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalSerializationApi::class)
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
    @JsonNames("checkInCount")
    @ColumnInfo(defaultValue = "0") val amount: Int,
    val note: String? = null
)
