package de.lijucay.damier.core.presentation.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.components.CheckInItem
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.models.CheckInUi
import de.lijucay.damier.core.presentation.models.toDisplayableDate
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInHistory(
    sheetState: SheetState,
    checkIns: Map<LocalDate, List<CheckInUi>>,
    unitId: UnitId,
    usesLimitColors: Boolean,
    onDismissRequest: () -> Unit,
    onItemClicked: (CheckInUi) -> Unit
) {
    val context = LocalContext.current
    val allDates = if (checkIns.isEmpty()) emptyList() else {
        val first = checkIns.keys.min()
        val today = LocalDate.now()

        generateSequence(today) { it.minusDays(1) }
            .takeWhile { !it.isBefore(first) }
            .toList()
    }

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        sheetGesturesEnabled = true,
        contentWindowInsets = {
            BottomSheetDefaults
                .modalWindowInsets
                .exclude(WindowInsets.navigationBars)
        },
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BottomSheetDefaults.DragHandle()
                Text(
                    text = stringResource(R.string.full_history),
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(bottom = bottomPadding())
        ) {
            allDates.forEach { date ->
                val checkInUis = checkIns[date]
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = date.toDisplayableDate().formatted,
                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                if (checkInUis.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .animateItem()
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.nothing_logged_yet),
                                style = typography.bodyLarge,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    items(checkInUis) { checkIn ->
                        CheckInItem(
                            modifier = Modifier.animateItem(),
                            checkInUi = checkIn,
                            unitName = if (checkIn.amount > 1)
                                unitId.getLongUnitNamesById(context).pluralName
                            else unitId.getLongUnitNamesById(context).singularName,
                            isItemFirst = checkInUis.first() == checkIn,
                            isItemLast = checkInUis.last() == checkIn,
                            usesLimitColors = usesLimitColors,
                            onClick = { onItemClicked(checkIn) }
                        )
                    }
                }
            }
        }
    }
}