package de.lijucay.damier.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.bottomPadding
import kotlinx.coroutines.launch

@Composable
fun OnBoarding(
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()

    val pages: List<@Composable () -> Unit> = listOf(
        { OnBoardingWelcomePage() },
        { OnBoardingActivityTypesPage() },
        { OnBoardingColorsPage() },
        { OnBoardingCheckingIn() },
        { OnBoardingGetStartedPage() }
    )

    val pagerState = rememberPagerState { pages.size }

    Scaffold(
        modifier = modifier,
        bottomBar = {
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
                        enabled = pagerState.currentPage != 0,
                        onClick = {
                            if (pagerState.currentPage != 0) {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            }
                        }
                    ) {
                        Text(text = stringResource(R.string.back))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(pages.size) { index ->
                            val isSelected = pagerState.currentPage == index

                            Box(modifier = Modifier.padding(4.dp)
                                .width(if (isSelected) 18.dp else 8.dp)
                                .height(if (isSelected) 8.dp else 8.dp)
                                .background(
                                    color = if (isSelected)
                                        MaterialTheme.colorScheme.tertiaryContainer
                                    else MaterialTheme.colorScheme.primaryContainer,
                                    shape = CircleShape
                                )
                            )
                        }
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage == pages.size - 1) {
                                onFinished()
                            } else {
                                scope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            }
                        }
                    ) {
                        Text(
                            text = if (pagerState.currentPage == pages.size - 1)
                                stringResource(R.string.done)
                            else
                                stringResource(R.string.next)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { page ->
            pages[page]()
        }
    }
}