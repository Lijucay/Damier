package de.lijucay.damier.di

import android.content.Context
import androidx.room.Room
import de.lijucay.damier.activity_details.presentation.ActivityDetailsViewModel
import de.lijucay.damier.activity_details.presentation.CheckInFormViewModel
import de.lijucay.damier.activity_list.presentation.ActivityListViewModel
import de.lijucay.damier.core.DataPreferences.dataStore
import de.lijucay.damier.core.data.ActivityRepositoryImpl
import de.lijucay.damier.core.data.DamierDatabase
import de.lijucay.damier.core.data.ExportUtilImpl
import de.lijucay.damier.core.data.ImportUtilImpl
import de.lijucay.damier.core.domain.StreakDataSource
import de.lijucay.damier.core.data.StreakDataSourceImpl
import de.lijucay.damier.core.domain.ActivityRepository
import de.lijucay.damier.core.domain.ExportUtil
import de.lijucay.damier.core.domain.ImportUtil
import de.lijucay.damier.core.presentation.viewmodels.ActivityFormViewModel
import de.lijucay.damier.core.presentation.viewmodels.UIViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            DamierDatabase::class.java,
            "damier_data"
        ).build()
    }

    single { get<DamierDatabase>().activityInfoDao() }
    single { get<DamierDatabase>().checkInDao() }
    single { get<DamierDatabase>().streakDao() }

    singleOf(::ImportUtilImpl).bind<ImportUtil>()
    singleOf(::ExportUtilImpl).bind<ExportUtil>()

    single { get<Context>().dataStore }

    singleOf(::StreakDataSourceImpl).bind<StreakDataSource>()
    singleOf(::ActivityRepositoryImpl).bind<ActivityRepository>()

    viewModelOf(::ActivityListViewModel)
    viewModelOf(::UIViewModel)
    viewModelOf(::ActivityFormViewModel)
    viewModelOf(::ActivityDetailsViewModel)
    viewModelOf(::CheckInFormViewModel)
}