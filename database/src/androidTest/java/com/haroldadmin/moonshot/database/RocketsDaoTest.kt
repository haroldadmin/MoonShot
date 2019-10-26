package com.haroldadmin.moonshot.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.models.SearchQuery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
internal class RocketsDaoTest: DaoTest() {

    private val rocketsDao: RocketsDao by dbRule.dao()
    private val launchesDao: LaunchDao by dbRule.dao()

    @Test
    fun allRocketsTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Rockets
            .many()
            .take(count)
            .toList()
            .also { rocketsDao.saveAll(it) }

        val expected = samples.sortedBy { it.rocketName }
        val actual = rocketsDao.all(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun rocketDetailsTest() = runBlocking {
        val sample = SampleDbData.Rockets.one().also { rocketsDao.save(it) }

        val expected = sample
        val actual = rocketsDao.one(rocketId = sample.rocketId)

        assertEquals(expected, actual)
    }

    @Test
    fun launchesForRocketTest() = runBlocking {
        val count = 20
        val rocketId = "falcon9"
        val sampleLaunches = SampleDbData.Launches
            .many(rocketIdGenerator = { rocketId }, isUpcomingGenerator = { Random.nextBoolean() })
            .take(count)
            .toList()
            .also { launchesDao.saveAll(it) }

        val sampleRocket = SampleDbData.Rockets
            .one(rocketId = rocketId)
            .also { rocketsDao.save(it) }

        val expected = sampleLaunches.filter { it.isUpcoming == false }.sortedByDescending { it.flightNumber }
        val actual = rocketsDao.launchesForRocket(rocketId = sampleRocket.rocketId, limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun searchRocketsTest() = runBlocking {
        val count = 20
        val searchQuery = SearchQuery("Falcon")

        val samples = SampleDbData.Rockets
            .many()
            .take(count)
            .toList()
            .also { rocketsDao.saveAll(it) }

        val expected = samples
            .filter { it.rocketName.contains(searchQuery.query) }
            .sortedBy { it.rocketName }

        val actual = rocketsDao.forQuery(query = searchQuery.sqlQuery(), limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun synchronizeTest() = runBlocking {
        val count = 20
        val rocketId1 = "id1"
        val rocketId2 = "id2"

        var samples = SampleDbData.Rockets
            .many(rocketIdGenerator = { rocketId1 })
            .take(count)
            .toList()

        rocketsDao.synchronizeBlocking(samples)

        samples = SampleDbData.Rockets
            .many(rocketIdGenerator = { rocketId2 })
            .take(count)
            .toList()

        rocketsDao.synchronizeBlocking(samples)

        val actual = rocketsDao.all(limit = count)

        assertTrue(actual.all { it.rocketId == rocketId2 })
    }
}