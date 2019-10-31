package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshotRepository.mappers.toDbMission
import com.haroldadmin.spacex_api_wrapper.SampleApiData
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class PersistMissionUseCaseTest {

    private lateinit var usecase: PersistMissionUseCase
    private lateinit var dao: FakeMissionDao

    @Before
    fun setup() {
        dao = FakeMissionDao()
        usecase = PersistMissionUseCase(dao)
    }

    @Test
    fun `should persist given DB mission`() = runBlocking {
        val missionId = "F3364BF"
        val mission = SampleDbData.Missions.one(id = missionId)
        usecase.persist(mission)

        val expected = mission
        val actual = dao.forId(missionId)

        assertEquals(expected, actual)
    }

    @Test
    fun `should persist given API mission`() = runBlocking {
        val missionId = "F3364BF"
        val mission = SampleApiData.Missions.one(id = missionId)
        usecase.persist(mission)

        val expected = mission.toDbMission()
        val actual = dao.forId(missionId)

        assertEquals(expected, actual)
    }

    @Test
    fun `should persist given DB missions`() = runBlocking {
        val count = 10
        val missions = SampleDbData.Missions.many()
            .take(count)
            .sortedBy { it.name }
            .toList()
            .also { usecase.persist(it) }

        val expected = missions
        val actual = dao.all(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun `should persist given API Missions`() = runBlocking {
        val count = 10
        val missions = SampleApiData.Missions.many()
            .take(count)
            .sortedBy { it.name }
            .toList()
            .also { usecase.persist(it) }

        val expected = missions.map { it.toDbMission() }
        val actual = dao.all(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun `should remove launches not in API response when synchronizing`() = runBlocking {
        val count = 10
        val obsoleteId = "obsolete"
        SampleDbData.Missions.many(idGenerator = { obsoleteId })
            .take(count)
            .toList()
            .sortedBy { it.name }
            .also { missions -> usecase.persist(missions) }

        val newId = "new"
        val expected = SampleDbData.Missions.many(idGenerator = { newId })
            .take(count)
            .toList()
            .sortedBy { it.name }
            .also { missions -> usecase.persist(missions, shouldSynchronize = true) }

        val actual = dao.all(limit = count)

        assertEquals(expected, actual)
    }

}