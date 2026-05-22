package de.lijucay.damier.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.bottomPadding

@Composable
fun OnBoardingButtons(
    isBackEnabled: Boolean,
    onBackClicked: () -> Unit,
    pages: Int,
    currentPage: Int,
    continueText: String,
    onContinueClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = bottomPadding())
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                enabled = isBackEnabled,
                onClick = onBackClicked
            ) {
                Text(text = stringResource(R.string.back))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pages) { index ->
                    val isSelected = currentPage == index

                    Box(modifier = Modifier.padding(4.dp)
                        .width(if (isSelected) 18.dp else 8.dp)
                        .height(if (isSelected) 8.dp else 8.dp)
                        .background(
                            color = if (isSelected)
                                MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                    )
                }
            }

            Button(onClick = onContinueClicked) { Text(text = continueText) }
        }
    }
}