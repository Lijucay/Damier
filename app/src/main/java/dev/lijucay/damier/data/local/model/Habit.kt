package dev.lijucay.damier.data.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val singularUnitName: String = "",
    val pluralUnitName: String = "",
    val goal: Int
)
