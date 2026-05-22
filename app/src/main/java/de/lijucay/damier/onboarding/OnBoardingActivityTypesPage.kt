package de.lijucay.damier.onboarding

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TonalToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.domain.DataUtil.toStringResource
import de.lijucay.damier.core.presentation.components.cards.PreviewCard
import de.lijucay.damier.core.presentation.getRandomCheckInInfo
import de.lijucay.damier.core.presentation.models.ActivityFormState
import de.lijucay.damier.design.components.DefaultText
import de.lijucay.damier.design.components.LargeTitleText
import de.lijucay.damier.design.components.TitleText
import de.lijucay.damier.shared.ReferenceType
import de.lijucay.damier.ui.theme.ActivityTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnBoardingActivityTypesPage(modifier: Modifier = Modifier) {
    var selectedReferenceType by remember { mutableStateOf(ReferenceType.entries.first()) }
    val labels = ReferenceType.entries.map { stringResource(it.toStringResource()) }

    val waffleDiagramData = remember { getRandomCheckInInfo(0, 50) }

    ActivityTheme(
        useLimitTheme = selectedReferenceType.isLimit()
    ) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LargeTitleText(text = stringResource(R.string.three_types_of_activities))

            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
            ) {
                val modifiers =
                    listOf(Modifier.weight(1f), Modifier.weight(1f), Modifier.weight(1f))

                ReferenceType.entries.forEachIndexed { index, type ->
                    TonalToggleButton(
                        checked = selectedReferenceType == type,
                        onCheckedChange = { selectedReferenceType = type },
                        modifier = modifiers[index].semantics { role = Role.RadioButton },
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            ReferenceType.entries.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        }
                    ) {
                        Text(labels[index])
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Crossfade(selectedReferenceType) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        TitleText(
                            text = stringResource(
                                id = when (it) {
                                    ReferenceType.GOAL ->  R.string.goal
                                    ReferenceType.LIMIT -> R.string.limit
                                    ReferenceType.MAX -> R.string.max
                                }
                            )
                        )
                        DefaultText(
                            text = stringResource(
                                id = when (it) {
                                    ReferenceType.GOAL -> R.string.goal_desc
                                    ReferenceType.LIMIT -> R.string.limit_desc
                                    ReferenceType.MAX -> R.string.max_desc
                                }
                            )
                        )
                    }
                }
            }

            TitleText(
                text = stringResource(R.string.example_board)
            )

            PreviewCard(
                state = ActivityFormState(
                    checkInInfo = waffleDiagramData
                )
            )
        }
    }
}