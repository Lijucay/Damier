package dev.lijucay.damier.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Habit(
    @PrimaryKey(autoGenerate = false) val title: String,
    val description: String? = null,
    val singularUnitName: String = "",
    val pluralUnitName: String = "",
    val goal: Int
)
