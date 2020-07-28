package com.haroldadmin.moonshot.features.launch.next

import com.haroldadmin.moonshot.core.Result
import com.haroldadmin.moonshot.core.TestDispatchers
import com.haroldadmin.moonshot.db.test.useDatabase
import com.haroldadmin.moonshot.db.toDBModel
import com.haroldadmin.moonshot.services.spacex.v4.LaunchesService
import com.haroldadmin.moonshot.services.spacex.v4.test.APISampleData
import com.haroldadmin.moonshot.services.spacex.v4.test.useJSONAdapter
import com.haroldadmin.moonshot.services.spacex.v4.Launch as APILaunch
import com.haroldadmin.moonshot.services.spacex.v4.test.useMockService
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import io.kotlintest.specs.AnnotationSpec
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

internal class NextLaunchUsecaseTest : AnnotationSpec() {

    @Test
    fun `refresh should result in an error if API response is invalid`() = runBlocking {
        val (db, dbCleanup) = useDatabase()
        val (launchesService, serviceCleanup) = useMockService<LaunchesService> {
            setBody("")
        }
        val dispatchers = TestDispatchers()
        val launchQueries = db.launchQueries
        val usecase = NextLaunchUsecase(launchQueries, launchesService, dispatchers)

        val result = usecase.refresh()
        result.shouldBeTypeOf<Result.Error<APILaunch>>()

        dbCleanup()
        serviceCleanup()
    }

    @Test
    fun `refresh result should be successful if API response is valid`() = runBlocking {
        val (db, dbCleanup) = useDatabase()
        val (launchesService, serviceCleanup) = useMockService<LaunchesService> {
            val launch = APISampleData.Launches.samples().first().copy(upcoming = true)
            val adapter = useJSONAdapter<APILaunch>()
            setBody(adapter.toJson(launch))
        }
        val launchQueries = db.launchQueries
        val dispatchers = TestDispatchers()
        val usecase = NextLaunchUsecase(launchQueries, launchesService, dispatchers)

        val result = usecase.refresh()
        result.shouldBeTypeOf<Result.Success<APILaunch>>()

        dbCleanup()
        serviceCleanup()
    }

    @Test
    fun `flow should emit a new value after refresh`() = runBlocking {
        val oldLaunch = APISampleData.Launches.samples()
            .first()
            .copy(upcoming = true, flightNumber = 1)
        val newLaunch = APISampleData.Launches.samples()
            .first()
            .copy(upcoming = true, flightNumber = 0)

        val (db, dbCleanup) = useDatabase()
        val launchQueries = db.launchQueries

        val mockServer = MockWebServer()
        val (launchesService, serviceCleanup) = useMockService<LaunchesService>(mockServer)

        val dispatchers = TestDispatchers()
        val usecase = NextLaunchUsecase(launchQueries, launchesService, dispatchers)

        // Save old launch to the database so that it is emitted first
        launchQueries.save(oldLaunch.toDBModel())

        usecase.flow().take(1).collect() shouldBe oldLaunch.toDBModel()

        // Prepare response for an API request for the next launch
        mockServer.enqueue(MockResponse().apply {
            val adapter = useJSONAdapter<APILaunch>()
            setBody(adapter.toJson(newLaunch))
        })

        // Refresh the launch in the database
        usecase.refresh().shouldBeTypeOf<Result.Success<APILaunch>>()
        // Assert that the newly refreshed launch is emitted now
        usecase.flow().first() shouldBe newLaunch.toDBModel()

        dbCleanup()
        serviceCleanup()
    }
}