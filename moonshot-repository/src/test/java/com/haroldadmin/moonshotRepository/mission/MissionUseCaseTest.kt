package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.TestDispatchers
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.Mission
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshotRepository.ExpectedResponse
import io.kotlintest.matchers.types.shouldBeTypeOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MissionUseCaseTest {

    private lateinit var dao: FakeMissionDao
    private lateinit var service: FakeMissionService
    private lateinit var persister: PersistMissionUseCase
    private lateinit var usecase: GetMissionUseCase
    private var missionId = "F3364BF"

    private val dispatchers = TestDispatchers()

    @Before
    fun setup() {
        dao = FakeMissionDao()
        service = FakeMissionService()
        persister = PersistMissionUseCase(dao, dispatchers)
        usecase = GetMissionUseCase(dao, service, persister, dispatchers)
    }

    @Test
    fun `should fetch mission for the given ID`() = runBlocking {
        service.expectedResponse = ExpectedResponse.Success
        dao.save(SampleDbData.Missions.one(id = missionId))

        val flow = usecase.getMissionForId(missionId)

        val emittedRes = flow.last()
        with(emittedRes) {
            shouldBeTypeOf<Resource.Success<Mission>>()
            this as Resource.Success

            assertEquals(missionId, this().id)
        }
    }
}