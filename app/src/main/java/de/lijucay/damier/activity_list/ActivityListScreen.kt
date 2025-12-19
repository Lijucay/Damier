package de.lijucay.damier.activity_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.bottomPadding
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.models.ActivityUi
import de.lijucay.damier.core.presentation.paddingWithSafeNavigationBar

@Composable
fun ActivityListScreen(
    modifier: Modifier = Modifier,
    isWidthAtLeastExpanded: Boolean,
    activityList: List<ActivityUi>,
) {
    ScreenContainer(
        modifier = modifier.fillMaxSize(),
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        title = stringResource(R.string.app_name),
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .paddingWithSafeNavigationBar(),
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_activity)
                )
            }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = bottomPadding() + 70.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(activityList) { activityUi ->
                ActivityListItem(activityUi = activityUi) { }
            }
        }
    }
}