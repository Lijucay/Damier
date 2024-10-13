package dev.lijucay.damier.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.lijucay.damier.BuildConfig
import dev.lijucay.damier.R
import dev.lijucay.damier.presentation.viewmodels.HabitViewModel
import dev.lijucay.damier.presentation.viewmodels.TrackingInfoViewModel
import dev.lijucay.damier.presentation.viewmodels.UIViewModel
import dev.lijucay.damier.util.Specs.bottomPadding
import dev.lijucay.damier.util.Specs.canGoBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiViewModel: UIViewModel,
    habitViewModel: HabitViewModel,
    trackingInfoViewModel: TrackingInfoViewModel,
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    val currentHabitID by habitViewModel.currentSelectedHabit.collectAsState()
    val currentTitle by uiViewModel.currentTitle.collectAsState()

    val tertiaryColor = MaterialTheme.colorScheme.tertiary

    val currentVersionName = BuildConfig.VERSION_NAME
    val currentVersionCode = BuildConfig.VERSION_CODE

    val cornerRadius by animateDpAsState(
        targetValue = if (currentRoute != Screens.HabitList.route && currentRoute != null) 40.dp
        else 0.dp,
        animationSpec = tween(500, easing = LinearEasing),
        label = "MainBoxBottomCornerRadius"
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing.exclude(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = currentTitle?.ifBlank { stringResource(R.string.app_name) } ?: stringResource(R.string.app_name)
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
                ),
                actions = {
                    AnimatedVisibility(
                        visible =
                            currentRoute == Screens.HabitList.route,
                        enter = expandHorizontally() + fadeIn(),
                        exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.SettingsScreen.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Settings,
                                contentDescription = stringResource(R.string.settings)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible =
                            currentRoute == Screens.HabitDetailScreen.route &&
                        currentHabitID != null,
                        enter = expandHorizontally() + fadeIn(),
                        exit = shrinkHorizontally() + fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                currentHabitID?.let {
                                    habitViewModel.setCurrentSelectedHabit(it)
                                    uiViewModel.setShowEditHabitDialog(true)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = stringResource(R.string.edit)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible =
                        currentRoute == Screens.HabitDetailScreen.route &&
                        currentHabitID != null,
                        enter = expandHorizontally() + fadeIn(),
                        exit = shrinkHorizontally() + fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                currentHabitID?.let {
                                    navController.popBackStack()
                                    habitViewModel.deleteHabit(it)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = stringResource(R.string.remove_habit)
                            )
                        }
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = currentRoute != Screens.HabitList.route && currentRoute != null,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        IconButton(
                            modifier = Modifier.padding(start = 16.dp),
                            onClick = {
                                if (navController.canGoBack)
                                    navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIosNew,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            AnimatedVisibility(
                currentRoute == Screens.SettingsScreen.route,
                enter = expandVertically(tween(500, easing = LinearEasing)),
                exit = shrinkVertically(tween(500, easing = LinearEasing))
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = 8.dp,
                            end = 8.dp,
                            bottom = bottomPadding() + 8.dp
                        )
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.app_version_info, currentVersionName, currentVersionCode)
                )
            }
            AnimatedVisibility(
                currentRoute == Screens.HabitDetailScreen.route &&
                currentHabitID != null,
                enter = expandVertically(animationSpec = tween(500, easing = LinearEasing)),
                exit = shrinkVertically(animationSpec = tween(500, easing = LinearEasing))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    IconButton(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 16.dp,
                                bottom = bottomPadding() + 16.dp
                            )
                            .size(80.dp)
                            .drawWithCache {
                                val star = RoundedPolygon.star(
                                    12,
                                    radius = size.minDimension * 0.45f,
                                    innerRadius = size.minDimension * 0.4f,
                                    centerX = size.width / 2,
                                    centerY = size.width / 2,
                                    rounding = CornerRounding(
                                        size.minDimension / 8f,
                                        smoothing = 0.3f
                                    )
                                )

                                val roundedPolygonStar = star.toPath().asComposePath()

                                onDrawBehind {
                                    drawPath(roundedPolygonStar, color = tertiaryColor)
                                }
                            },
                        onClick = {
                            uiViewModel.setShowCounterDialog(true)
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Bolt,
                            null,
                            tint = contentColorFor(tertiaryColor)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute == Screens.HabitList.route,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = bottomPadding()),
                    onClick = {
                        uiViewModel.setShowAddHabitDialog(true)
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.add_habit)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .clip(RoundedCornerShape(
                    topStart = 40.dp,
                    topEnd = 40.dp,
                    bottomEnd = cornerRadius,
                    bottomStart = cornerRadius
                ))
                .background(MaterialTheme.colorScheme.surface)
                .animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .animateContentSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screens.HabitList.route
                ) {
                    composable(Screens.HabitList.route) {
                        HabitListScreen(
                            uiViewModel = uiViewModel,
                            habitViewModel = habitViewModel,
                            trackingInfoViewModel = trackingInfoViewModel,
                            onHabitClicked = { habitId ->
                                habitViewModel.setCurrentSelectedHabit(habitId)
                                navController.navigate(Screens.HabitDetailScreen.route)
                            },
                            onButtonPressed = { habitId ->
                                habitViewModel.setCurrentSelectedHabit(habitId)
                                uiViewModel.setShowCounterDialog(true)
                            }
                        )
                    }

                    composable(Screens.HabitDetailScreen.route) {
                        currentHabitID?.let { id ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                HabitDetailsScreen(
                                    id = id,
                                    trackingInfoViewModel = trackingInfoViewModel,
                                    uiViewModel = uiViewModel,
                                    habitViewModel = habitViewModel
                                )
                            }
                        }
                    }

                    composable(Screens.SettingsScreen.route) {
                        SettingsScreen(
                            uiViewModel = uiViewModel,
                            onOpenLicenseScreen = {},
                        )
                    }
                }
            }
        }
    }
}