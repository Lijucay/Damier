package dev.lijucay.damier.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Specs {
    val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val yearMonthFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    @Composable
    fun bottomPadding(): Dp =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    @Composable
    fun topPadding(): Dp =
        WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val NavHostController.canGoBack: Boolean
        get() = this.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED
}