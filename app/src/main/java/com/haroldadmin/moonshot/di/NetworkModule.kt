package com.haroldadmin.moonshot.di

import android.content.Context
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.haroldadmin.moonshot.BuildConfig
import com.haroldadmin.moonshot.base.di.V4API
import com.haroldadmin.moonshot.db.LocalDateAdapter
import com.haroldadmin.moonshot.db.ZonedDateTimeAdapter
import com.haroldadmin.moonshot.services.spacex.v4.LaunchesService
import com.haroldadmin.spacex_api_wrapper.BASE_URL
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @V4API
    fun retrofit(context: Context): Retrofit {
        val moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(ZonedDateTimeAdapter())
            .build()

        val cache = Cache(context.cacheDir, 10 * 1024 * 1024)

        val okHttp = OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    val logger = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(logger)
                }
            }
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .client(okHttp)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    @V4API
    fun launchesService(@V4API retrofit: Retrofit): LaunchesService {
        return retrofit.create(LaunchesService::class.java)
    }
}