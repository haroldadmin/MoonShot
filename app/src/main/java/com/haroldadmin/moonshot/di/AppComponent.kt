package com.haroldadmin.moonshot.di

import android.content.Context
import android.content.SharedPreferences
import com.haroldadmin.moonshot.MainActivity
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.MoonShot
import com.haroldadmin.moonshot.MoonShotWorkerFactory
import com.haroldadmin.moonshot.base.di.MoonShotActivityComponent
import com.haroldadmin.moonshot.database.LaunchDao
import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.database.RocketsDao
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshotRepository.RepositoryModule
import com.haroldadmin.moonshotRepository.applicationInfo.ApplicationInfoUseCase
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Component(modules = [AppModule::class, NotificationModule::class, RepositoryModule::class])
@Singleton
interface AppComponent : MoonShotActivityComponent<MainActivity> {

    fun inject(app: MoonShot)

    fun workerFactory(): MoonShotWorkerFactory
    fun appInfoUseCase(): ApplicationInfoUseCase
    fun syncManager(): SyncManager
    fun launchNotificationsManager(): LaunchNotificationsManager

    @Named("settings")
    fun settings(): SharedPreferences

    fun mainViewModelFactory(): MainViewModel.Factory

    fun launchDao(): LaunchDao
    fun rocketsDao(): RocketsDao
    fun launchPadDao(): LaunchPadDao

    fun launchService(): LaunchesService
    fun rocketsService(): RocketsService
    fun launchPadsService(): LaunchPadService

    fun broadcastReceiversComponent(): BroadcastReceiversComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
