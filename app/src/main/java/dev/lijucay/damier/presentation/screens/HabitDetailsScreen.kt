package dev.lijucay.damier.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.lijucay.damier.R
import dev.lijucay.damier.presentation.composables.LastSevenDaysGraph
import dev.lijucay.damier.presentation.composables.TrackingInfoCard
import dev.lijucay.damier.presentation.composables.WaffleCardItem
import dev.lijucay.damier.presentation.viewmodels.HabitViewModel
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.presentation.viewmodels.UIViewModel
import dev.lijucay.damier.util.Specs.yearMonthFormat
import dev.lijucay.damier.util.Utils.groupTrackingInfoByMonth
import dev.lijucay.damier.util.Utils.prepareLastSevenDaysData

@Composable
fun HabitDetailsScreen(
    id: Int?,
    uiViewModel: UIViewModel,
    habitViewModel: HabitViewModel,
    trackingInfoViewModel: TrackingInfoViewModel,
) {
    val habitList by habitViewModel.habitList.collectAsState()
    val habit = id?.let { habitList.find { habit -> habit.id == it } }

    val habitTitle = habit?.title

    val habitTrackingInfo by trackingInfoViewModel.trackingInfoMap.collectAsState()

    LaunchedEffect(habitTrackingInfo) {
        Log.d("HabitDetailsScreen", "Habit tracking info got changed: $habitTrackingInfo")
    }

    val trackingInfo = habitTrackingInfo[id]

    LaunchedEffect(habitTitle) { uiViewModel.setCurrentTitle(habitTitle) }

    trackingInfo?.let { tis ->
        habit?.let {
            val groupedTrackingInfo = groupTrackingInfoByMonth(tis)
            val lastSevenDaysData = prepareLastSevenDaysData(tis)

            LazyColumn {
                item {
                    Column {
                        WaffleCardItem(
                            modifier = Modifier.fillMaxWidth(),
                            showTitle = false,
                            habit = habit,
                            trackingInfoViewModel = trackingInfoViewModel
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                        ) {
                            LastSevenDaysGraph(lastSevenDaysData, habit.goal)
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = stringResource(id = R.string.check_ins),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (groupedTrackingInfo.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            text = stringResource(R.string.no_tracking_info)
                        )
                    }
                } else {
                    groupedTrackingInfo.forEach { (yearMonth, trackingInfoList) ->
                        var checkInCount by mutableIntStateOf(0)
                        trackingInfoList.forEach { ti -> checkInCount += ti.count }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = yearMonth.format(yearMonthFormat),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                                Text(
                                    text = checkInCount.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        items(trackingInfoList, key = { it.id }) { ti ->
                            TrackingInfoCard(
                                modifier = Modifier
                                    .animateItem()
                                    .padding(
                                        top = if (trackingInfoList.first() == ti) 16.dp else 0.dp,
                                        bottom = if (trackingInfoList.last() == ti) 0.dp else 4.dp
                                    )
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(
                                    topStart = if (trackingInfoList.first() == ti) 20.dp else 8.dp,
                                    topEnd = if (trackingInfoList.first() == ti) 20.dp else 8.dp,
                                    bottomEnd = if (trackingInfoList.last() == ti) 20.dp else 8.dp,
                                    bottomStart = if (trackingInfoList.last() == ti) 20.dp else 8.dp
                                ),
                                trackingInfo = ti,
                                singularUnitName = habit.singularUnitName,
                                pluralUnitName = habit.pluralUnitName
                            ) {
                                trackingInfoViewModel.removeTrackingInfo(habit.id, ti)
                            }
                        }
                    }
                }
            }
        }
    }
}