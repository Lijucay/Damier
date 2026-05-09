package de.lijucay.damier

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import de.lijucay.damier.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DamierApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = false
        }

        startKoin {
            logger(AndroidLogger(level = Level.DEBUG))
            androidContext(this@DamierApplication)
            modules(appModule)
        }
    }
}