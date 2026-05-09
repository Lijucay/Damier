package de.lijucay.damier.activity_list.presentation

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
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.WaffleDiagramData
import de.lijucay.damier.core.domain.ReferenceType
import de.lijucay.damier.core.presentation.components.WaffleDiagram
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.LargeTitleText

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
            LargeTitleText(
                modifier = Modifier.fillMaxWidth(),
                text = activityUi.title,
                textAlign = TextAlign.Center
            )

            WaffleDiagram(
                waffleDiagramData = WaffleDiagramData(
                    reference = activityUi.reference,
                    referenceType = activityUi.referenceType,
                    checkIns = activityUi.groupedCheckIns.values.flatten()
                )
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
                    LargeText(text = "${activityUi.referenceType}: ${activityUi.reference}")
                }
                AnimatedVisibility(
                    visible = activityUi.referenceType == ReferenceType.MAX
                            && showReference
                            && showMaxAmount
                ) {
                    LargeText(
                        text = "Max: ${activityUi.groupedCheckIns.maxOfOrNull { (_, checkIns) -> checkIns.sumOf { it.amount } } ?: 0}",
                        )
                }

                AnimatedContent(
                    targetState = showReference && if (activityUi.referenceType.isMax()) showMaxAmount else true
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
                            DefaultText(text = stringResource(R.string.check_in))
                        }
                    }
                }
            }
        }
    }
}
