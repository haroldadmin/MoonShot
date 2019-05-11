package com.haroldadmin.spacex_api_wrapper.capsule

import com.haroldadmin.cnradapter.NetworkResponse
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

internal class CapsuleTest : KoinTest {

    lateinit var server: MockWebServer
    lateinit var service: CapsuleService

    @Before
    fun setup() {
        startKoin { modules(testModule) }
        server = MockWebServer()
        server.start()
        service = get<Retrofit> { parametersOf(server) }.create(CapsuleService::class.java)
    }

    @After
    fun teardown() {
        stopKoin()
        server.close()
    }

    @Test
    fun `Get all capsules should be successful`() = runBlocking {
        val responseBody: String = getResource("/sampledata/capsules/all_capsules_response.json").readText()
        server.enqueue(
            MockResponse().apply {
                setBody(responseBody)
                setResponseCode(200)
            }
        )
        val response = service.getAllCapsules().await()
        assertTrue(response is NetworkResponse.Success)
        assertTrue((response as NetworkResponse.Success).body.isNotEmpty())
        assertTrue(server.takeRequest().path == "/capsules?sort=original_launch&order=desc")
    }

    @Test
    fun `Get one capsule should be successful`() = runBlocking {
        val responseBody = getResource("/sampledata/capsules/one_capsule_response.json").readText()
        server.enqueue(
            MockResponse().apply {
                setBody(responseBody)
                setResponseCode(200)
            }
        )
        val serial = "C112"
        val response = service.getCapsule(serial).await()
        assertTrue(response is NetworkResponse.Success)
        assertTrue((response as NetworkResponse.Success<Capsule>).body.serial == serial)
    }
}