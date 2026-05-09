package de.lijucay.damier.activity_details.presentation.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.CompareArrows
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.ActivityDetailsState
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.SmallText

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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
                LargeText(text = stringResource(R.string.statistics))
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Timeline,
                            contentDescription = null
                        )
                        DefaultText(
                            text = stringResource(R.string.timeline),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    SmallText(
                        text = stringResource(R.string.timeline_expl)
                    )

                    TimelineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        state = state
                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.CompareArrows,
                            contentDescription = null
                        )
                        DefaultText(
                            text = stringResource(R.string.week_comparison),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    SmallText(text = stringResource(R.string.week_comparison_expl))

                    ComparisonChart(
                        modifier = Modifier
                            .height(260.dp)
                            .padding(vertical = 16.dp),
                        state = state
                    )
                }
            }
        }

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