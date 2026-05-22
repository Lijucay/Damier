package de.lijucay.damier.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.Cell
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.shared.ReferenceType
import java.time.LocalDate

@Composable
fun OnBoardingColorsPage(modifier: Modifier = Modifier) {
    val cDate = LocalDate.now()
    val eDate = cDate.plusDays(1)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LargeTitleText(text = stringResource(R.string.how_colors_work))

        DefaultText(text = stringResource(R.string.how_colors_work_expl))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.max_and_goal)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(10) {
                        Cell(
                            modifier = Modifier.weight(1f),
                            checkInCount = it,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 9,
                            type = ReferenceType.GOAL,
                        )
                        Spacer(Modifier.width(if (it == 9) 0.dp else 6.dp))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefaultText(
                        text = stringResource(R.string.less)
                    )

                    DefaultText(
                        text = stringResource(R.string.more)
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.limit)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(10) {
                        Cell(
                            modifier = Modifier.weight(1f),
                            checkInCount = it,
                            currentDate = cDate,
                            endDate = eDate,
                            reference = 9,
                            type = ReferenceType.LIMIT,
                        )
                        Spacer(Modifier.width(if (it == 9) 0.dp else 6.dp))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DefaultText(
                        text = stringResource(R.string.under_limit)
                    )

                    DefaultText(
                        text = stringResource(R.string.at_limit)
                    )
                }
            }
        }
    }
}