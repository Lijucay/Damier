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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.design.components.HeadlineText
import de.lijucay.damier.design.components.LargeText
import de.lijucay.damier.design.components.SmallText
import de.lijucay.damier.design.components.TitleText

@Composable
fun StreakCard(
    modifier: Modifier = Modifier,
    currentStreak: Int,
//    longestStreak: Int
) {
    val resources = LocalResources.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
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

                TitleText(text = stringResource(R.string.streak))

                Spacer(modifier = Modifier.width(6.dp))

                Box(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.extraLarge)
                        .background(color = MaterialTheme.colorScheme.onSecondaryContainer)
                ) {
                    SmallText(
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
                        color = MaterialTheme.colorScheme.secondaryContainer
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
                    HeadlineText(text = streak.toString())
                }

                LargeText(
                    text = stringResource(
                        id = if (currentStreak == 1) R.string.day
                        else R.string.days
                    )
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