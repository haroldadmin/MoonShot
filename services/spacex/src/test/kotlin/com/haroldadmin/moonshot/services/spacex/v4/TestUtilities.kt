package com.haroldadmin.moonshot.services.spacex.v4

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.haroldadmin.moonshot.services.spacex.v4.adapters.LocalDateAdapter
import com.haroldadmin.moonshot.services.spacex.v4.adapters.ZonedDateTimeAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

internal inline fun <reified T> useMockService(responseBuilder: MockResponse.() -> Unit): Pair<T, () -> Unit> {
    val mockServer = MockWebServer()
    val moshi = Moshi.Builder()
        .add(LocalDateAdapter())
        .add(ZonedDateTimeAdapter())
        .build()
    val retrofit = Retrofit.Builder()
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(mockServer.url("/"))
        .build()

    val service = retrofit.create(T::class.java)

    val response = MockResponse().apply(responseBuilder)
    mockServer.enqueue(response)

    return service to { mockServer.shutdown() }
}

internal inline fun <reified T> useJSONAdapter(): JsonAdapter<T> {
    val moshi = Moshi.Builder()
        .add(LocalDateAdapter())
        .add(ZonedDateTimeAdapter())
        .build()

    return moshi.adapter(T::class.java)
}

internal fun useJSON(path: String): String {
    return Any::class.java.getResource(path).readText()
}
