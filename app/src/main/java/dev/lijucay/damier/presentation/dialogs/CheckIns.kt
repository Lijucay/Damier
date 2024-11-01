package dev.lijucay.damier.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.lijucay.damier.R
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.presentation.composables.TrackingInfoCard
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.util.Specs.topPadding
import dev.lijucay.damier.util.Specs.yearMonthFormat
import dev.lijucay.damier.util.Utils.groupTrackingInfoByMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckIns(
    sheetState: SheetState,
    habit: Habit,
    trackingInfoViewModel: TrackingInfoViewModel,
    onDismissRequest: () -> Unit,
) {
    val habitTrackingInfo by trackingInfoViewModel.trackingInfoMap.collectAsStateWithLifecycle()
    val habitTrackingInfoList = habitTrackingInfo[habit.title]

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = null
    ) {
        val uiSystemController = rememberSystemUiController()
        uiSystemController.setNavigationBarColor(Color.Transparent)
        uiSystemController.isNavigationBarContrastEnforced = false

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding())
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.check_ins),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            habitTrackingInfoList?.let { trackingInfo ->
                val groupedTrackingInfo = groupTrackingInfoByMonth(trackingInfo)

                LazyColumn {
                    if (groupedTrackingInfo.isEmpty()) {
                        item {

                        }
                    } else {
                        groupedTrackingInfo.forEach { (yearMonth, trackingInfoList) ->
                            var checkInCount by mutableIntStateOf(0)
                            trackingInfoList.forEach { checkInCount += it.count }

                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = yearMonth.format(yearMonthFormat),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = checkInCount.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            items(trackingInfoList, key = { "${it.habitTitle}_${it.date}_${it.time}" }) { trackingInfo ->
                                TrackingInfoCard(
                                    modifier = Modifier
                                        .animateItem()
                                        .padding(
                                            top = if (trackingInfoList.first() == trackingInfo) 16.dp else 0.dp,
                                            bottom = if (trackingInfoList.last() == trackingInfo) 0.dp else 4.dp,
                                            start = 16.dp,
                                            end = 16.dp
                                        )
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(
                                        topStart = if (trackingInfoList.first() == trackingInfo) 20.dp else 8.dp,
                                        topEnd = if (trackingInfoList.first() == trackingInfo) 20.dp else 8.dp,
                                        bottomEnd = if (trackingInfoList.last() == trackingInfo) 20.dp else 8.dp,
                                        bottomStart = if (trackingInfoList.last() == trackingInfo) 20.dp else 8.dp
                                    ),
                                    trackingInfo = trackingInfo,
                                    singularUnitName = habit.singularUnitName,
                                    pluralUnitName = habit.pluralUnitName
                                ) {
                                    trackingInfoViewModel.removeTrackingInfo(habit.title, trackingInfo)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}