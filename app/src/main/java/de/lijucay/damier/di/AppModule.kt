package de.lijucay.damier.di

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.room.Room
import de.lijucay.damier.R
import de.lijucay.damier.activity_details.presentation.ActivityDetailsScreen
import de.lijucay.damier.activity_details.presentation.ActivityDetailsViewModel
import de.lijucay.damier.activity_details.presentation.CheckInFormViewModel
import de.lijucay.damier.activity_details.presentation.EditActivityScreen
import de.lijucay.damier.activity_list.presentation.ActivityListScreen
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.activity_list.presentation.AddActivityItemScreen
import de.lijucay.damier.core.DataPreferences.dataStore
import de.lijucay.damier.core.data.ActivityRepositoryImpl
import de.lijucay.damier.core.data.DamierDatabase
import de.lijucay.damier.core.data.ExportUtilImpl
import de.lijucay.damier.core.data.ImportUtilImpl
import de.lijucay.damier.core.data.Migrations
import de.lijucay.damier.core.data.StreakDataSourceImpl
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.ExportUtil
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.core.domain.StreakDataSource
import de.lijucay.damier.core.presentation.Destination
import de.lijucay.damier.core.presentation.Navigator
import de.lijucay.damier.core.presentation.viewmodels.ActivityFormViewModel
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import de.lijucay.damier.nfc.NfcManager
import de.lijucay.damier.nfc.NfcViewModel
import de.lijucay.damier.nfc.RecentNfcWriteTracker
import de.lijucay.damier.nfc.read.CueReadManager
import de.lijucay.damier.nfc.read.NfcReadManager
import de.lijucay.damier.nfc.write.NfcWriteManager
import de.lijucay.damier.settings.presentation.SettingsScreen
import de.lijucay.damier.widget.data.WidgetRepositoryImpl
import de.lijucay.damier.widget.domain.WidgetRepository
import de.lijucay.damier.widget.presentation.DamierWidgetState
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.scopedOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3AdaptiveApi::class)
val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DamierDatabase::class.java,
            "damier_data"
        ).addMigrations(
            Migrations.Migration_1_2,
            Migrations.Migration_2_3,
            Migrations.Migration_3_4,
            Migrations.Migration_4_5
        ).build()
    }

    single { get<DamierDatabase>().activityInfoDao() }
    single { get<DamierDatabase>().checkInDao() }
    single { get<DamierDatabase>().streakDao() }
    single { get<DamierDatabase>().nfcChipDao() }

    singleOf(::NfcWriteManager)
    singleOf(::NfcReadManager)
    singleOf(::CueReadManager)

    singleOf(::ImportUtilImpl).bind<ImportUtil>()
    singleOf(::ExportUtilImpl).bind<ExportUtil>()

    single { get<Context>().dataStore }

    singleOf(::StreakDataSourceImpl).bind<StreakDataSource>()
    singleOf(::ActivityRepositoryImpl).bind<ActivityRepository>()
    singleOf(::WidgetRepositoryImpl).bind<WidgetRepository>()
    singleOf(::DamierWidgetState)

    singleOf(::RecentNfcWriteTracker)

    activityRetainedScope {
        scopedOf(::NfcManager)

        viewModelOf(::UIViewModel)
        viewModelOf(::NfcViewModel)

        scoped { Navigator(startDestination = Destination.ActivityList) }
        scoped { SnackbarHostState() }

        navigation<Destination.ActivityList>(
            metadata = ListDetailSceneStrategy.listPane(
                detailPlaceholder = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_activity_selected))
                    }
                }
            )
        ) {
            val navigator = get<Navigator>()
            ActivityListScreen(
                snackbarHost = { SnackbarHost(get<SnackbarHostState>()) },
                onActivityClicked = { id ->
                    navigator.goTo(
                        Destination.ActivityDetails(
                            id.toString()
                        )
                    )
                },
                onSettingsClicked = { navigator.goTo(Destination.Settings) },
                onAddActivity = { navigator.goTo(Destination.AddActivity) }
            )
        }

        navigation<Destination.ActivityDetails>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { route ->
            val navigator = get<Navigator>()
            ActivityDetailsScreen(
                activityId = route.activityId,
                onEditActivity = { activity ->
                    navigator.goTo(Destination.EditActivity(activity.id.toString()))
                },
                scope = this,
                onNavigateBack = { navigator.goBack() }
            )
        }

        navigation<Destination.AddActivity>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) {
            val navigator = get<Navigator>()
            AddActivityItemScreen { navigator.goBack() }
        }

        navigation<Destination.EditActivity>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) { route ->
            val navigator = get<Navigator>()
            EditActivityScreen(
                activityId = route.activityId,
                onNavigateBack = { navigator.goBack() }
            )
        }

        navigation<Destination.Settings>(
            metadata = ListDetailSceneStrategy.detailPane()
        ) {
            val navigator = get<Navigator>()
            val context = get<Context>()
            val snackbarHostState = get<SnackbarHostState>()
            val scope = rememberCoroutineScope()

            SettingsScreen(
                snackbarHost = { SnackbarHost(get<SnackbarHostState>()) },
                showCrashlyticsChangedSnackbar = { snackbarSumId, show, buttonTxtId, action ->
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = context.getString(snackbarSumId),
                            actionLabel = if (show) context.getString(buttonTxtId) else null,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            action()
                        }
                    }
                },
                scope = this,
                onNavigateBack = { navigator.goBack() }
            )
        }
    }

    viewModelOf(::ActivityListViewModel)
    viewModelOf(::ActivityFormViewModel)
    viewModelOf(::ActivityDetailsViewModel)
    viewModelOf(::CheckInFormViewModel)
}
