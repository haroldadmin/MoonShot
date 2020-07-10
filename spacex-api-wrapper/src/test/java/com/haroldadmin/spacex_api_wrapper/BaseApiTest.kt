package com.haroldadmin.spacex_api_wrapper

import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import io.kotlintest.Spec
import io.kotlintest.specs.DescribeSpec
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date

internal abstract class BaseApiTest : DescribeSpec() {

    protected lateinit var server: MockWebServer
    protected lateinit var moshi: Moshi
    protected lateinit var retrofit: Retrofit

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        server = MockWebServer()
        moshi = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
            .build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(server.url("/"))
            .build()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        server.close()
    }
}