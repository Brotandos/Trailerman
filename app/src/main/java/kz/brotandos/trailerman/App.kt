package kz.brotandos.trailerman

import android.app.Application
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            this.applicationContext, listOf(
                uiModule,
                retrofitModule,
                serviceModule,
                databaseModule,
                repositoryModule
            )
        )

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }
}
