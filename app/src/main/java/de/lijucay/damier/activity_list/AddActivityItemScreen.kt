package de.lijucay.damier.activity_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.domain.UnitId
import de.lijucay.damier.core.domain.getLongUnitNamesById
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.models.ActivityUi
import java.util.UUID

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AddActivityItemScreen(
    modifier: Modifier = Modifier,
    isWidthAtLeastExpanded: Boolean
) {
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

    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.add_activity)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            // Top-Padding:
            Spacer(modifier = Modifier.height(16.dp))

            // Preview
            ActivityListItem(
                modifier = Modifier.fillMaxWidth(),
                activityUi = finalActivity
            ) {}

            // Title field
            Text(text = title)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = unitId.getLongUnitNamesById(context).singularName)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}