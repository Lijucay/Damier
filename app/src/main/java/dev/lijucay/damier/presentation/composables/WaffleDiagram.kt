package dev.lijucay.damier.presentation.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun WaffleDiagram(
    modifier: Modifier = Modifier,
    habit: Habit,
    trackingInfoViewModel: TrackingInfoViewModel,
) {
    val habitWaffleBoardInfo by trackingInfoViewModel.waffleBoardInfoMap.collectAsState()

    LaunchedEffect(habitWaffleBoardInfo) {
        Log.d("WaffleDiagram", "Waffle data was updated: $habitWaffleBoardInfo")
    }

    val waffleDiagramData = habitWaffleBoardInfo[habit.title]

    val currentDate = LocalDate.now()
    val startDate = currentDate.minusWeeks(13).with(DayOfWeek.MONDAY)

    val dataMap = waffleDiagramData?.associateBy { it.date }

    Column(modifier = modifier) {
        DayOfWeek.entries.forEach { dayOfWeek ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(14) { weekIndex ->
                    val date = startDate.plusWeeks(weekIndex.toLong()).with(dayOfWeek)
                    val count = dataMap?.get(date)?.totalCount ?: 0
                    val goal = habit.goal // 100 check ins is our backup

                    val intensity = if (goal > 0) count.toFloat() / goal else 0f
                    val alpha = 0.1f + (0.9f * intensity)

                    val cellColor = if (
                            date.isBefore(currentDate) &&
                            count != 0
                        ) MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                        else if (
                            date.isEqual(currentDate) &&
                            count != 0
                        ) MaterialTheme.colorScheme.tertiary.copy(alpha = alpha)
                        else MaterialTheme.colorScheme.primaryContainer

                    val borderColor = if (
                            count == 0 &&
                            date.isBefore(currentDate)
                        ) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.tertiary

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .background(color = cellColor, shape = RoundedCornerShape(5.dp))
                            .then(
                                if (count == 0 && !date.isAfter(currentDate)) Modifier.border(
                                    width = 1.dp,
                                    color = borderColor,
                                    shape = RoundedCornerShape(5.dp)
                                ) else Modifier
                            )
                    )
                }
            }
        }
    }
}