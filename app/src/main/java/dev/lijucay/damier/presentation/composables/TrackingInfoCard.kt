package dev.lijucay.damier.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.lijucay.damier.R
import dev.lijucay.damier.data.local.model.TrackingInfo
import dev.lijucay.damier.util.Specs.dateFormat
import dev.lijucay.damier.util.Specs.timeFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TrackingInfoCard(
    modifier: Modifier = Modifier,
    trackingInfo: TrackingInfo,
    shape: Shape,
    singularUnitName: String,
    pluralUnitName: String,
    onDeleteRequest: () -> Unit
) {
    val currentDate = LocalDate.now()

    val tiDate = LocalDate.parse(trackingInfo.date)
    val tiTime = LocalTime.parse(trackingInfo.time)

    val unitName = if (trackingInfo.count > 1) pluralUnitName
    else singularUnitName

    val dayOfWeekText = if (tiDate.isEqual(currentDate))
        stringResource(R.string.today)
    else if (currentDate.minusDays(1).isEqual(tiDate))
        stringResource(R.string.yesterday)
    else tiDate.dayOfWeek.getDisplayName(
        TextStyle.FULL, Locale.getDefault()
    ).replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }

    val dateText = "$dayOfWeekText, ${tiDate.format(dateFormat)}"
    val timeText = tiTime.format(timeFormat)

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = dateText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = timeText)
                Text(text = "${trackingInfo.count} $unitName")
            }
            IconButton(onClick = onDeleteRequest) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(
                        R.string.delete_tracking_info, dateText, timeText
                    )
                )
            }
        }
    }
}