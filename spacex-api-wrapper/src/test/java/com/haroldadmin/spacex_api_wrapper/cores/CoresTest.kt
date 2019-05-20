package com.haroldadmin.spacex_api_wrapper.cores

import com.haroldadmin.cnradapter.NetworkResponse
import com.haroldadmin.spacex_api_wrapper.core.CoresService
import com.haroldadmin.spacex_api_wrapper.getResource
import com.haroldadmin.spacex_api_wrapper.testModule
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.test.KoinTest
import org.koin.test.get
import retrofit2.Retrofit

internal class CoresTest: KoinTest {

    lateinit var server: MockWebServer
    lateinit var service: CoresService

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        server = MockWebServer()
        server.start()
        service = get<Retrofit> { parametersOf(server) }.create(CoresService::class.java)
    }

    @Test
    fun `Get all cores should return a list of cores`() = runBlocking {
        val responseBody = getResource("/sampledata/cores/all_cores.json").readText()
        server.enqueue(
            MockResponse().apply {
                setBody(responseBody)
                setResponseCode(200)
            }
        )
        val response = service.getAllCores().await()
        assertTrue(response is NetworkResponse.Success)
        assertTrue((response as NetworkResponse.Success).body.isNotEmpty())
    }

    @Test
    fun `Get core by serial should return core with that serial`() = runBlocking {
        val responseBody = getResource("/sampledata/cores/one_core.json").readText()
        server.enqueue(
            MockResponse().apply {
                setBody(responseBody)
                setResponseCode(200)
            }
        )
        val serial = "B1042"
        val response = service.getCore(serial).await()
        assertTrue(response is NetworkResponse.Success)
        assertTrue((response as NetworkResponse.Success).body.serial == serial)

    }

    @After
    fun teardown() {
        stopKoin()
        server.close()
    }
}