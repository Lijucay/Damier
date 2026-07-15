package de.lijucay.damier.core.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.LocalDateTimeConverter
import de.lijucay.damier.core.data.converter.LocalDateTimeSerializer
import de.lijucay.damier.core.data.converter.UUIDSerializer
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
            onDelete = CASCADE
        )
    ],
    indices = [Index("activityId")]
)
@TypeConverters(LocalDateTimeConverter::class)
data class NfcChipInfo(
    @PrimaryKey(autoGenerate = false)
    @Serializable(with = UUIDSerializer::class)
    val chipId: UUID,
    @Serializable(with = UUIDSerializer::class)
    val activityId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    val linkedAt: LocalDateTime,
    val label: String? = null
)
