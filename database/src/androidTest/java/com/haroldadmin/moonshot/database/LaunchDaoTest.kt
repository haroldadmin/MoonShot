package com.haroldadmin.moonshot.database

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.models.SampleDbData
import com.haroldadmin.moonshot.models.SearchQuery
import com.haroldadmin.moonshot.models.launch.Launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
internal class LaunchDaoTest : DaoTest() {

    private val dao: LaunchDao by dbRule.dao()

    @Test
    fun launchDetailsTest() = runBlocking {
        dao.save(SampleDbData.Launches.one())

        val sample = SampleDbData.Launches.one()
        val retrieved = dao.details(sample.flightNumber)

        assertEquals(sample, retrieved)
    }

    @Test
    fun nextLaunchTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { Random.nextInt() % 2 == 0 })
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val expected = samples
            .filter { it.isUpcoming == true }
            .sortedBy { it.flightNumber }
            .first()

        val actual = dao.next()

        assertEquals(expected, actual)
    }

    @Test
    fun allLaunchesTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches.many().take(count).toList()
        dao.saveAll(samples)

        val actual = dao.all(limit = count)

        assertEquals(samples, actual)
    }

    @Test
    fun upcomingLaunchesTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { Random.nextInt() % 2 == 0 })
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val expected = samples.filter { it.isUpcoming == true }.sortedBy { it.flightNumber }
        val actual = dao.upcoming(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun pastLaunchesTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { Random.nextInt() % 2 == 0 })
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val expected = samples.filter { it.isUpcoming == false }.sortedByDescending { it.flightNumber }
        val actual = dao.recent(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun allLaunchesForLaunchpadTest() = runBlocking {

        val count = 20
        val samples = SampleDbData.Launches
            .many()
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val testSiteId = 10.toString()

        val expected = samples.first { it.launchSite!!.siteId == testSiteId }
        val actual = dao.forLaunchPad(siteId = testSiteId, limit = count)

        assertTrue(actual.size == 1)
        assertEquals(expected, actual[0])
    }

    @Test
    fun pastLaunchesForLaunchPadTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { Random.nextInt() % 2 == 0 })
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val testSiteId = 10.toString()

        val expected = samples.filter { it.launchSite!!.siteId == testSiteId && it.isUpcoming == false }
        val actual = dao.forLaunchPad(siteId = testSiteId, isUpcoming = false, limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun upcomingLaunchesForLaunchPadTest() = runBlocking {
        val count = 20
        val samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { Random.nextInt() % 2 == 0 })
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val testSiteId = 10.toString()
        val expected = samples.filter { it.launchSite!!.siteId == testSiteId && it.isUpcoming == true }
        val actual = dao.forLaunchPad(siteId = testSiteId, isUpcoming = true, limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun launchPicturesTest() = runBlocking {
        val sample = SampleDbData.Launches.one().also { dao.save(it) }

        val expected = sample.links!!.flickrImages!!
        val actual = dao.pictures(sample.flightNumber)!!.flickrImages

        assertEquals(expected, actual)
    }

    @Test
    fun clearTableTest() = runBlocking {
        val count = 10
        val samples = SampleDbData.Launches.many().take(count).toList()
        dao.apply {
            saveAll(samples)
            clearTable()
        }

        val expected = emptyList<Launch>()
        val actual = dao.all(limit = count)

        assertEquals(expected, actual)
    }

    @Test
    fun synchronizeTest() = runBlocking {
        val count = 10

        var samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { false })
            .take(count)
            .toList()

        dao.synchronizeBlocking(samples)

        samples = SampleDbData.Launches
            .many(isUpcomingGenerator = { true })
            .take(count)
            .toList()

        dao.synchronizeBlocking(samples)

        val actual = dao.all(limit = count)

        assertTrue(actual.all { it.isUpcoming == true })
    }

    @Test
    fun searchLaunchesTest() = runBlocking {
        val count = 20
        val searchQuery = SearchQuery("Telstar")

        val samples = SampleDbData.Launches
            .many()
            .take(count)
            .toList()
            .also { dao.saveAll(it) }

        val expected = samples
            .filter { it.missionName.contains(searchQuery.query) }
            .sortedBy { it.missionName }

        val actual = dao.forQuery(searchQuery.sqlQuery(), limit = count)

        assertEquals(expected, actual)
    }
}