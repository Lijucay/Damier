package de.lijucay.damier.di

import android.content.Context
import androidx.room.Room
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.core.Preferences.dataStore
import de.lijucay.damier.core.data.DamierDatabase
import de.lijucay.damier.core.data.StreakDataSource
import de.lijucay.damier.core.data.StreakDataSourceImpl
import de.lijucay.damier.core.presentation.UIViewModel
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

    single { get<Context>().dataStore }

    singleOf(::StreakDataSourceImpl).bind<StreakDataSource>()

    viewModelOf(::ActivityListViewModel)
    viewModelOf(::UIViewModel)
}