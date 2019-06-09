package com.haroldadmin.moonshot

import android.app.Application
import com.airbnb.epoxy.Carousel
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class MoonShot : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            logger(AndroidLogger())
            androidContext(this@MoonShot.applicationContext)
            modules(appModule)
        }
        Carousel.setDefaultGlobalSnapHelperFactory(null)
    }
}