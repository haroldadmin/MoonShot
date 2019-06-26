package com.haroldadmin.moonshot

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.airbnb.epoxy.Carousel
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.vector.Vector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import kotlin.coroutines.CoroutineContext

class MoonShot : Application(), CoroutineScope {

    companion object {
        const val MOONSHOT_SHARED_PREFS = "moonshot-shared-prefs"
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(AndroidLogger())
            androidContext(this@MoonShot.applicationContext)
            modules(appModule)
        }

        Carousel.setDefaultGlobalSnapHelperFactory(null)

        if (BuildConfig.DEBUG) {
            Vector.enableLogging()
        }

        launch {
            initializeWorkManager(this@MoonShot)
            get<SyncManager>().enableSync()
        }
    }

    private suspend fun initializeWorkManager(context: Context) = withContext(Dispatchers.Default) {
        val workerFactory = get<MoonShotWorkerFactory>()
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(context, config)
    }
}