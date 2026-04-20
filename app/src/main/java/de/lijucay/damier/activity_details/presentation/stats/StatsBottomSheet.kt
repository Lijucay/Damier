package de.lijucay.damier.activity_details.presentation.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import de.lijucay.damier.core.data.wrapper.toCheckInInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsBottomSheet(
    onDismissRequest: () -> Unit,
    dialogState: SheetState,
    state: ActivityDetailsState
) {
    ModalBottomSheet(
        sheetState = dialogState,
        onDismissRequest = onDismissRequest,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BottomSheetDefaults.DragHandle()
                Text(
                    text = stringResource(R.string.statistics),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    ) {
        TimelineGraph(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            state = state
        )

        // Todo: Check-ins per week day per week
        //  (pie-chart for weekdays vs. weekends,
        //  column chart for all 7 weekdays with comparison to previous week)
        //  => get check ins from last monday, (maxOf() with(Day.MONDAY))
        //  => get check ins from monday before (maxOf() minusWeeks(1) with(Day.MONDAY))
        //  => get totals per day for both week
        //  => Column graph allows multiple columns per unit, create two cols, one showing the last
        //     week, one showing the current week
    }
}