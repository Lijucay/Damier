package dev.lijucay.damier.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.lijucay.damier.R
import dev.lijucay.damier.data.local.model.TrackingInfo
import dev.lijucay.damier.presentation.composables.WaffleCardItem
import dev.lijucay.damier.presentation.viewmodels.HabitViewModel
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.presentation.viewmodels.UIViewModel
import dev.lijucay.damier.util.ResponseState
import dev.lijucay.damier.util.Specs.bottomPadding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HabitListScreen(
    uiViewModel: UIViewModel,
    habitViewModel: HabitViewModel,
    trackingInfoViewModel: TrackingInfoViewModel,
    onHabitClicked: (Int) -> Unit, // current habit id
    onButtonPressed: (Int) -> Unit // current habit id
) {
    val context = LocalContext.current

    val responseState by habitViewModel.responseState.collectAsState()
    LaunchedEffect(Unit) { uiViewModel.setCurrentTitle(context.getString(R.string.app_name)) }

    AnimatedContent(
        responseState,
        transitionSpec = {
            fadeIn(tween(durationMillis = 300)) togetherWith fadeOut(
                tween(
                    durationMillis = 300
                )
            )
        },
        label = ""
    ) { state ->
        when (state) {
            ResponseState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stringResource(R.string.no_habits_yet))
                }
            }
            is ResponseState.Failure -> { /* no-op */ }
            ResponseState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            ResponseState.Success -> {
                val habits by habitViewModel.habitList.collectAsState()
                val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
                val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

                LazyColumn(
                    contentPadding = PaddingValues(bottom = bottomPadding())
                ) {
                    items(habits, key = { it.id }) { habit ->
                        WaffleCardItem(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(
                                    top = if (habits.indexOf(habit) != -1 && habits.indexOf(
                                            habit
                                        ) != 0
                                    ) 24.dp else 0.dp
                                ),
                            habit = habit,
                            trackingInfoViewModel = trackingInfoViewModel,
                            onCardClicked = { onHabitClicked(habit.id) },
                            onLongButtonPressed = { onButtonPressed(habit.id) },
                            onButtonPressed = {
                                trackingInfoViewModel.insertTrackingInfo(TrackingInfo(
                                    habitId = habit.id,
                                    date = LocalDate.now().format(dateFormatter),
                                    time = LocalTime.now().format(timeFormatter),
                                    count = 1
                                ))
                            }
                        )
                    }
                }
            }
        }
    }


//    val habits by damierViewModel.habitList.collectAsState()
//
//    val habitTrackingInfoMap by damierViewModel.habitWaffleBoardInfoMap.collectAsState()
//    val areHabitsEmpty by damierViewModel.areHabitsEmpty.collectAsState()
//
//    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
//    val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME
//
//    var showList by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) { damierViewModel.setCurrentHabitTitle(context.getString(R.string.app_name)) }
//    LaunchedEffect(habitTrackingInfoMap) {
//        if (habitTrackingInfoMap.isNotEmpty())
//            showList = true
//    }
//
//    AnimatedContent(
//        targetState = showList,
//        transitionSpec = {
//            fadeIn(tween(durationMillis = 300)) togetherWith fadeOut(tween(durationMillis = 300))
//        },
//        label = "loading list animation"
//    ) { state ->
//        when (state) {
//            true -> AnimatedContent(targetState = areHabitsEmpty, label = "empty list animation") { s ->
//                when (s) {
//                    true -> Box(
//                        modifier = Modifier.fillMaxSize(),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = stringResource(R.string.no_habits_yet))
//                    }
//                    false -> LazyColumn(
//                        contentPadding = PaddingValues(bottom = bottomPadding())
//                    ) {
//                        items(habits) { habit ->
//                            WaffleCardItem(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(
//                                        top = if (habits.indexOf(habit) != -1 && habits.indexOf(
//                                                habit
//                                            ) != 0
//                                        ) 24.dp else 0.dp
//                                    ),
//                                habit = habit,
//                                damierViewModel = damierViewModel,
//                                onCardClicked = { onHabitClicked(habit.id) },
//                                onLongButtonPressed = { onButtonPressed(habit.id) },
//                                onButtonPressed = {
//                                    damierViewModel.insertTrackingInfo(TrackingInfo(
//                                        habitId = habit.id,
//                                        date = LocalDate.now().format(dateFormatter),
//                                        time = LocalTime.now().format(timeFormatter),
//                                        count = 1
//                                    ))
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//            false -> Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }

//    Box {
//        AnimatedVisibility(
//            modifier = Modifier.zIndex(1f),
//            visible = !showList && !areHabitsEmpty,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//        AnimatedVisibility(areHabitsEmpty) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(text = stringResource(R.string.no_habits_yet))
//            }
//        }
//        AnimatedVisibility(
//            modifier = Modifier.zIndex(2f),
//            visible = showList && !areHabitsEmpty,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            LazyColumn(
//                contentPadding = PaddingValues(bottom = bottomPadding())
//            ) {
//                items(habits) { habit ->
//                    WaffleCardItem(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(
//                                top = if (habits.indexOf(habit) != -1 && habits.indexOf(habit) != 0) 24.dp else 0.dp
//                            ),
//                        habit = habit,
//                        damierViewModel = damierViewModel,
//                        onCardClicked = { onHabitClicked(habit.id) },
//                        onLongButtonPressed = { onButtonPressed(habit.id) },
//                        onButtonPressed = {
//                            damierViewModel.insertTrackingInfo(TrackingInfo(
//                                habitId = habit.id,
//                                date = LocalDate.now().format(dateFormatter),
//                                time = LocalTime.now().format(timeFormatter),
//                                count = 1
//                            ))
//                        }
//                    )
//                }
//            }
//        }
//    }
}