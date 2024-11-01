package dev.lijucay.damier.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["habitTitle", "date", "time"],
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["title"],
            childColumns = ["habitTitle"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["habitTitle"])]
)
data class TrackingInfo(
    val habitTitle: String,
    val date: String,
    val time: String,
    @ColumnInfo(defaultValue = "0") val count: Int
)
