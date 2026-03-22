package de.lijucay.damier.core.data.converter

import androidx.room.TypeConverter
import de.lijucay.damier.core.domain.UnitId

class UnitIdConverter {
    @TypeConverter
    fun toUnitId(unitString: String): UnitId {
        return UnitId.valueOf(unitString)
    }

    @TypeConverter
    fun toString(unitId: UnitId): String {
        return unitId.name
    }
}