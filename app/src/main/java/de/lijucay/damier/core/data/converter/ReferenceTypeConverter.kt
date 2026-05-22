package de.lijucay.damier.core.data.converter

import androidx.room.TypeConverter
import de.lijucay.damier.shared.ReferenceType

class ReferenceTypeConverter {
    @TypeConverter
    fun toReferenceType(typeString: String): ReferenceType {
        return ReferenceType.valueOf(typeString)
    }

    @TypeConverter
    fun toString(referenceType: ReferenceType): String {
        return referenceType.name
    }
}