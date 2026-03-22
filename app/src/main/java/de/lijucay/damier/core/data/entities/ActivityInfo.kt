package de.lijucay.damier.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.lijucay.damier.core.data.converter.ReferenceTypeConverter
import de.lijucay.damier.core.data.converter.UnitIdConverter
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import java.util.UUID

@Entity
@TypeConverters(UnitIdConverter::class, ReferenceTypeConverter::class)
data class ActivityInfo(
    @PrimaryKey(autoGenerate = false) val id: UUID = UUID.randomUUID(),
    val activityName: String,
    val unit: UnitId,
    val reference:Int,
    val referenceType: ReferenceType,
    val defaultAmount: Int
)
