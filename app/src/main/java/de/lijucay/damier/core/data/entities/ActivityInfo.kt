package de.lijucay.damier.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ActivityInfo(
    @PrimaryKey(autoGenerate = false) val id: UUID = UUID.randomUUID(),
    val activityName: String,
    val unit: String, // UnitId.name
    val reference:Int,
    val referenceType: String // ReferenceType.name
)
