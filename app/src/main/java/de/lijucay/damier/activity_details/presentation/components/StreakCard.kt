package de.lijucay.damier.activity_details.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R

@Composable
fun StreakCard(
    modifier: Modifier = Modifier,
    currentStreak: Int,
//    longestStreak: Int
) {
    val resources = LocalResources.current

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.secondaryContainer,
            contentColor = colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.current_streak),
                    style = typography.titleMedium
                )

                Box(
                    modifier = Modifier
                        .clip(shape = shapes.extraLarge)
                        .background(color = colorScheme.tertiaryContainer)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = resources.getStringArray(
                            when (currentStreak) {
                                0 -> R.array.streak_m_ns
                                in 1..7 -> R.array.streak_m_s
                                in 8..14 -> R.array.streak_m_m
                                in 15..Int.MAX_VALUE -> R.array.streak_m_l
                                else -> R.array.streak_m_ns
                            }
                        ).random(),
                        style = typography.bodySmall.copy(color = colorScheme.onTertiaryContainer)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnimatedContent(
                    targetState = currentStreak,
                    transitionSpec = {
                        (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                    }
                ) { streak ->
                    Text(
                        text = "$streak",
                        style = typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = stringResource(R.string.days),
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

//            HorizontalDivider()
//
//            Row(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = ImageVector.vectorResource(R.drawable.reward),
//                    contentDescription = null
//                )
//
//                Text(
//                    text = pluralStringResource(
//                        R.plurals.personal_best,
//                        longestStreak,
//                        longestStreak
//                    ),
//                    fontWeight = FontWeight.Bold
//                )
//            }
        }
    }
}