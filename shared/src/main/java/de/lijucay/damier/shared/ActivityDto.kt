package de.lijucay.damier.shared

data class ActivityDto(
    val id: String,
    val activityName: String,
    val defaultAmount: Int,
    val todayCheckIns: Int
)