package com.haroldadmin.spacex_api_wrapper

import com.haroldadmin.cnradapter.CoroutinesNetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date

val testModule = module {
    single<Moshi> {
        Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single<CoroutinesNetworkResponseAdapterFactory> {
        CoroutinesNetworkResponseAdapterFactory()
    }

    single<Retrofit> { (mockWebServer: MockWebServer) ->
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
            .addCallAdapterFactory(get<CoroutinesNetworkResponseAdapterFactory>())
            .baseUrl(mockWebServer.url("/"))
            .build()
    }
}