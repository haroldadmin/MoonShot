package com.haroldadmin.spacex_api_wrapper

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.haroldadmin.spacex_api_wrapper.capsule.CapsuleService
import com.haroldadmin.spacex_api_wrapper.core.CoresService
import com.haroldadmin.spacex_api_wrapper.dragon.DragonsService
import com.haroldadmin.spacex_api_wrapper.history.HistoryService
import com.haroldadmin.spacex_api_wrapper.info.InfoService
import com.haroldadmin.spacex_api_wrapper.landingpads.LandingPadsService
import com.haroldadmin.spacex_api_wrapper.launches.LaunchesService
import com.haroldadmin.spacex_api_wrapper.launchpad.LaunchPadService
import com.haroldadmin.spacex_api_wrapper.mission.MissionService
import com.haroldadmin.spacex_api_wrapper.payload.PayloadsService
import com.haroldadmin.spacex_api_wrapper.rocket.RocketsService
import com.haroldadmin.spacex_api_wrapper.v4.ZonedDateTimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import javax.inject.Singleton

@Module
object ApiModule {

    @Provides
    @Singleton
    fun retrofit(cache: Cache, logger: HttpLoggingInterceptor): Retrofit {
        val moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(ZonedDateTimeAdapter::class.java)
            .build()

        val okHttp = OkHttpClient.Builder()
            .addInterceptor(logger)
            .cache(cache)
            .build()

        return Retrofit.Builder()
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun capsuleService(retrofit: Retrofit): CapsuleService {
        return retrofit.create(CapsuleService::class.java)
    }

    @Provides
    @Singleton
    fun coresService(retrofit: Retrofit): CoresService {
        return retrofit.create(CoresService::class.java)
    }

    @Provides
    @Singleton
    fun dragonsService(retrofit: Retrofit): DragonsService {
        return retrofit.create(DragonsService::class.java)
    }

    @Provides
    @Singleton
    fun historyService(retrofit: Retrofit): HistoryService {
        return retrofit.create(HistoryService::class.java)
    }

    @Provides
    @Singleton
    fun infoService(retrofit: Retrofit): InfoService {
        return retrofit.create(InfoService::class.java)
    }

    @Provides
    @Singleton
    fun landingPadsService(retrofit: Retrofit): LandingPadsService {
        return retrofit.create(LandingPadsService::class.java)
    }

    @Provides
    @Singleton
    fun launchesService(retrofit: Retrofit): LaunchesService {
        return retrofit.create(LaunchesService::class.java)
    }

    @Provides
    @Singleton
    fun launchPadService(retrofit: Retrofit): LaunchPadService {
        return retrofit.create(LaunchPadService::class.java)
    }

    @Provides
    @Singleton
    fun missionService(retrofit: Retrofit): MissionService {
        return retrofit.create(MissionService::class.java)
    }

    @Provides
    @Singleton
    fun payloadsService(retrofit: Retrofit): PayloadsService {
        return retrofit.create(PayloadsService::class.java)
    }

    @Provides
    @Singleton
    fun rocketsService(retrofit: Retrofit): RocketsService {
        return retrofit.create(RocketsService::class.java)
    }
}