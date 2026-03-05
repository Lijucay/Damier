package de.lijucay.damier.di

import androidx.room.Room
import de.lijucay.damier.activity_list.ActivityListViewModel
import de.lijucay.damier.core.data.DamierDatabase
import de.lijucay.damier.core.presentation.UIViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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

    viewModelOf(::ActivityListViewModel)
    viewModelOf(::UIViewModel)
}