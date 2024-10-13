package dev.lijucay.damier.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.lijucay.damier.data.local.database.DamierDatabase
import dev.lijucay.damier.data.local.database.HabitDao
import dev.lijucay.damier.data.local.database.TrackingInfoDao
import dev.lijucay.damier.data.local.repository.HabitRepository
import dev.lijucay.damier.data.local.repository.HabitRepositoryImpl
import dev.lijucay.damier.data.local.repository.TrackingInfoRepository
import dev.lijucay.damier.data.local.repository.TrackingInfoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideDamierDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context = context,
            klass = DamierDatabase::class.java,
            "damier_database"
        )
            .build()

    @Singleton
    @Provides
    fun provideHabitDao(database: DamierDatabase) = database.habitDao()

    @Singleton
    @Provides
    fun provideTrackingInfoDao(database: DamierDatabase) = database.trackingInfoDao()

    @Singleton
    @Provides
    fun provideHabitRepository(habitDao: HabitDao): HabitRepository = HabitRepositoryImpl(habitDao)

    @Singleton
    @Provides
    fun provideTrackingInfoRepository(trackingInfoDao: TrackingInfoDao): TrackingInfoRepository =
        TrackingInfoRepositoryImpl(trackingInfoDao)
}