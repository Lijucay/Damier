package de.lijucay.damier.activity_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.lijucay.damier.core.domain.Activity
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.presentation.models.ActivityUi
import java.util.UUID

@Composable
fun AddActivityItemScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val activityId = UUID.randomUUID()

    var title by remember { mutableStateOf("") }
    var unitId by remember { mutableStateOf(UnitId.TIMES) }
    var reference by remember { mutableIntStateOf(0) }
    var referenceType by remember { mutableStateOf(ReferenceType.GOAL) }

    var finalActivity by remember(title, unitId, reference, referenceType) {
        mutableStateOf(
            ActivityUi(
                id = activityId,
                title = title,
                unitId = unitId,
                reference = reference,
                referenceType = referenceType
            )
        )
    }

    // Activity needs name, units (selected from an available list),

    // Preview

    // title field

    // Unit selection + reference (if reference 1, use singular unit name, else use plural unit name

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            top = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            ActivityListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                activityUi = finalActivity
            ) {}
        }

        item {

        }
    }
}