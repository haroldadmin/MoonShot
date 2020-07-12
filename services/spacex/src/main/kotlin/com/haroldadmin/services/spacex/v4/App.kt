package com.haroldadmin.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun main() {
    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val httpClient = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spacexdata.com/v4/")
        .client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()

    val service = retrofit.create(CapsuleService::class.java)

    runBlocking {
        when(val response = service.one("5e9e2c5bf35918ed873b2665")) {
            is NetworkResponse.Success -> {
                println("Success")
                println(response.body)
            }
            else -> println(response)
        }
    }
}