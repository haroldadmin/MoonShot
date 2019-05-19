package com.haroldadmin.spacex_api_wrapper

import io.kotlintest.Spec
import io.kotlintest.specs.DescribeSpec
import okhttp3.mockwebserver.MockWebServer
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit

abstract class BaseApiTest: KoinTest, DescribeSpec() {

    protected val server by lazy { MockWebServer() }
    protected val retrofit by inject<Retrofit> { parametersOf(server) }

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        startKoin {
            modules(testModule)
        }
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        stopKoin()
        server.close()
    }

}