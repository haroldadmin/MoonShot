package com.haroldadmin.moonshotRepository

import android.content.Context
import com.haroldadmin.moonshot.database.DatabaseModule
import com.haroldadmin.spacex_api_wrapper.ApiModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module(includes = [ApiModule::class, DatabaseModule::class])
object RepositoryModule {
    @Singleton
    @Provides
    fun okHttpCache(context: Context): Cache {
        return Cache(context.cacheDir, 10 * 1000 * 1000)
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
    }
}