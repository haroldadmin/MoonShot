package com.haroldadmin.moonshot

import android.app.Application
import androidx.work.Configuration
import com.airbnb.epoxy.Carousel
import com.haroldadmin.moonshot.models.ApplicationInfo
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import kotlin.coroutines.CoroutineContext

class MoonShot : Application(), Configuration.Provider, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val appInfoUseCase by inject<ApplicationInfoUseCase>()

    override fun getWorkManagerConfiguration(): Configuration {
        val workerFactory = MoonShotWorkerFactory()
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(AndroidLogger())
            androidContext(this@MoonShot.applicationContext)
            modules(appModule)
        }

        Carousel.setDefaultGlobalSnapHelperFactory(null)

        launch {
            if (appInfoUseCase.getApplicationInfo()?.isFirstLaunch != false) {
                get<SyncManager>().enableSync()
                get<LaunchNotificationsManager>().enable()
            } else {
                appInfoUseCase.update(ApplicationInfo(isFirstLaunch = false))
            }
        }
    }
}