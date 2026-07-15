package de.lijucay.damier.core.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey
import java.util.UUID

class Navigator(startDestination: NavKey) {
    val backStack = mutableStateListOf<Any>(startDestination)

    fun goTo(destination: NavKey) {
        backStack.add(destination)
    }

    fun goBack(): Boolean = backStack.removeLastOrNull() != null

    fun goToActivityDetailsFresh(activityId: UUID) {
        backStack.clear()
        backStack.add(Destination.ActivityList)
        backStack.add(Destination.ActivityDetails(activityId.toString()))
    }
}