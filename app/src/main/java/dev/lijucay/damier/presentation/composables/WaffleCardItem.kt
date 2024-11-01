package dev.lijucay.damier.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.lijucay.damier.R
import dev.lijucay.damier.data.local.model.Habit
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.util.DataStore.SHOW_GOAL
import dev.lijucay.damier.util.DataStore.dataStore
import kotlinx.coroutines.flow.map

@Composable
fun WaffleCardItem(
    modifier: Modifier = Modifier,
    habit: Habit,
    trackingInfoViewModel: TrackingInfoViewModel,
    showTitle: Boolean = true,
    onCardClicked: (() -> Unit)? = null,
    onLongButtonPressed: () -> Unit = {},
    onButtonPressed: (() -> Unit)? = null
) {
    val title = habit.title
    val context = LocalContext.current

    val showGoal by context.dataStore.data
        .map { preferences -> preferences[SHOW_GOAL] ?: false }
        .collectAsState(false)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        onClick = { onCardClicked?.invoke() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            if (showTitle) {
                LargeTitleText(
                    modifier = Modifier.padding(start = 4.dp),
                    text = title
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            WaffleDiagram(
                habit = habit,
                trackingInfoViewModel = trackingInfoViewModel
            )
            onButtonPressed?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (showGoal) Arrangement.SpaceBetween
                    else Arrangement.End
                ) {
                    if (showGoal) {
                        MediumTitleText(text = stringResource(R.string.goal, habit.goal))
                    }
                    Button(
                        modifier = Modifier,
                        onClick = onButtonPressed,
                        onLongPress = onLongButtonPressed,
                        shape = RoundedCornerShape(10.dp),
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            tint = contentColorFor(MaterialTheme.colorScheme.primary),
                            imageVector = Icons.Rounded.Bolt,
                            contentDescription = stringResource(R.string.check_in)
                        )
                    }
                }
            }
        }
    }
}