package dev.lijucay.damier.presentation.screens

sealed class Screens(val route: String) {
    data object HabitList : Screens("habitList")
    data object HabitDetailScreen : Screens("habitDetail")
    data object SettingsScreen : Screens("settings")
}