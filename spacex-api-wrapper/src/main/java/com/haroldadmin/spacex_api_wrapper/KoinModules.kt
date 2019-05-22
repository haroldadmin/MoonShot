package com.haroldadmin.spacex_api_wrapper

import com.haroldadmin.cnradapter.CoroutinesNetworkResponseAdapterFactory
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
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date

val networkModule = module {

    single<Moshi> {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<CoroutinesNetworkResponseAdapterFactory> {
        CoroutinesNetworkResponseAdapterFactory()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            )
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .client(get<OkHttpClient>())
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .addCallAdapterFactory(get<CoroutinesNetworkResponseAdapterFactory>())
            .baseUrl(BASE_URL)
            .build()
    }
}

val serviceModule = module {
    single<CapsuleService> { get<Retrofit>().create(CapsuleService::class.java) }
    single<CoresService> { get<Retrofit>().create(CoresService::class.java) }
    single<DragonsService> { get<Retrofit>().create(DragonsService::class.java) }
    single<HistoryService> { get<Retrofit>().create(HistoryService::class.java) }
    single<InfoService> { get<Retrofit>().create(InfoService::class.java) }
    single<LandingPadsService> { get<Retrofit>().create(LandingPadsService::class.java) }
    single<LaunchesService> { get<Retrofit>().create(LaunchesService::class.java) }
    single<LaunchPadService> { get<Retrofit>().create(LaunchPadService::class.java) }
    single<MissionService> { get<Retrofit>().create(MissionService::class.java) }
    single<PayloadsService> { get<Retrofit>().create(PayloadsService::class.java) }
    single<RocketsService> { get<Retrofit>().create(RocketsService::class.java) }
}