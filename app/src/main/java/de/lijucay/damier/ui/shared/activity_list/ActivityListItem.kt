package de.lijucay.damier.ui.shared.activity_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.ui.shared.core.WaffleDiagram
import de.lijucay.damier.core.presentation.models.ActivityUi

@Composable
fun ActivityListItem(
    modifier: Modifier = Modifier,
    activityUi: ActivityUi,
    showReference: Boolean,
    showMaxAmount: Boolean,
    onCheckInClicked: () -> Unit,
    onItemClick: () -> Unit,
) {
    val containerColor = if (activityUi.referenceType != ReferenceType.LIMIT)
        MaterialTheme.colorScheme.primaryContainer
    else MaterialTheme.colorScheme.errorContainer

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColorFor(containerColor)
        ),
        onClick = onItemClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = activityUi.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            WaffleDiagram(
                reference = activityUi.reference,
                type = activityUi.referenceType,
                checkIns = activityUi.groupedCheckIns.values.flatten()
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                itemVerticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = activityUi.referenceType != ReferenceType.MAX && showReference
                ) {
                    Text(
                        text = "${activityUi.referenceType}: ${activityUi.reference}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                AnimatedVisibility(
                    visible = activityUi.referenceType == ReferenceType.MAX
                            && showReference
                            && showMaxAmount
                ) {
                    Text(
                        text = "Max: ${activityUi.groupedCheckIns.maxOfOrNull { (_, checkIns) -> checkIns.sumOf { it.checkInCount } } ?: 0}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                AnimatedContent(
                    targetState = if (activityUi.referenceType == ReferenceType.MAX) showMaxAmount else showReference
                ) {
                    if (it) {
                        IconButton(
                            onClick = onCheckInClicked,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = contentColorFor(containerColor),
                                contentColor = containerColor
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Bolt,
                                contentDescription = stringResource(R.string.check_in)
                            )
                        }
                    } else {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(),
                            onClick = onCheckInClicked,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = contentColorFor(containerColor),
                                contentColor = containerColor
                            )
                        ) {
                            Text(text = stringResource(R.string.check_in))
                        }
                    }
                }
            }
        }
    }
}