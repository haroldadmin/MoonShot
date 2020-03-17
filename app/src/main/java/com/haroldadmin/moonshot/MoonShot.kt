package com.haroldadmin.moonshot

import android.app.Application
import android.os.Handler
import androidx.work.Configuration
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.logger.Logger
import com.haroldadmin.moonshot.di.AppComponentHolder
import com.haroldadmin.moonshot.di.DaggerAppComponent
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.models.ApplicationInfo
import com.haroldadmin.moonshot.models.isFirstLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import com.haroldadmin.whatthestack.WhatTheStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class MoonShot : Application(), Configuration.Provider, CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main

    @Inject lateinit var appInfoUseCase: ApplicationInfoUseCase
    @Inject lateinit var syncManager: SyncManager
    @Inject lateinit var notificationsManager: LaunchNotificationsManager
    @Inject @Named("epoxy-differ") lateinit var epoxyDiffer: Handler
    @Inject @Named("epoxy-builder") lateinit var epoxyBuilder: Handler

    override fun getWorkManagerConfiguration(): Configuration {
        val workerFactory = appComponent().workerFactory()
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }

    override fun onCreate() {
        initDi()
        super.onCreate()
        initLogger()
        initWhatTheStack()
        initEpoxy()
        launch {
            if (appInfoUseCase.getApplicationInfo().isFirstLaunch()) {
                syncManager.enableSync()
                notificationsManager.enable()
                appInfoUseCase.save(ApplicationInfo(isFirstLaunch = false))
            }
        }
    }

    private fun initDi() {
        val appComponent = DaggerAppComponent.factory().create(this)
        AppComponentHolder.init(appComponent)
        appComponent.inject(this)
    }

    private fun initEpoxy() {
        EpoxyController.defaultDiffingHandler = epoxyDiffer
        EpoxyController.defaultModelBuildingHandler = epoxyBuilder
        Carousel.setDefaultGlobalSnapHelperFactory(null)
    }

    private fun initWhatTheStack() {
        if (BuildConfig.DEBUG) {
            WhatTheStack(this).init()
        }
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Logger.enable()
        }
    }
}