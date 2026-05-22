package de.lijucay.damier.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.lijucay.damier.R
import de.lijucay.damier.core.presentation.components.OnBoardingButtons
import de.lijucay.damier.core.presentation.components.ScreenContainer
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import kotlinx.coroutines.launch

@Composable
fun OnBoarding(
    uiViewModel: UIViewModel,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isWidthAtLeastExpanded by uiViewModel.isWidthAtLeastExpanded.collectAsStateWithLifecycle()

    val pages: List<@Composable () -> Unit> = listOf(
        { OnBoardingWelcomePage() },
        { OnBoardingActivityTypesPage() },
        { OnBoardingColorsPage() },
        { OnBoardingCheckingIn() },
        { OnBoardingGetStartedPage() }
    )

    val pagerState = rememberPagerState { pages.size }

    ScreenContainer(
        isWidthAtLeastExpanded = isWidthAtLeastExpanded,
        topAppBarActions = {
            Button(
                onClick = onFinished,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(text = stringResource(R.string.skip))
            }
        },
        title = stringResource(R.string.welcome),
        showBottomBarContent = true,
        bottomBarContent = {
            OnBoardingButtons(
                isBackEnabled = pagerState.currentPage != 0,
                onBackClicked = {
                    if (pagerState.currentPage != 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                pages = pages.size,
                currentPage = pagerState.currentPage,
                continueText = if (pagerState.currentPage == pages.size - 1)
                    stringResource(R.string.done)
                else
                    stringResource(R.string.next),
                onContinueClicked = {
                    if (pagerState.currentPage == pages.size - 1) {
                        onFinished()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            )
        }
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            pages[page]()
        }
    }
}