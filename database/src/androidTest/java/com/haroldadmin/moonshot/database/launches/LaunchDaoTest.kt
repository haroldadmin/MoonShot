package com.haroldadmin.moonshot.database.launches

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class LaunchDaoTest : BaseDbTest() {

    private val launchDao by lazy { db.launchDao() }
    private val rocketSummaryDao by lazy { db.rocketSummaryDao() }
    private val secondStageSummaryDao by lazy { db.secondStageSummaryDao() }
    private val firstStageSummaryDao by lazy { db.firstStageSummaryDao() }
    private val launch = Launch.getSampleLaunch()
    private val rocketSummary = RocketSummary.getSampleRocketSummary(launch.flightNumber)
    private val secondStageSummary = SecondStageSummary.getSampleSecondStageSummary(launch.flightNumber)
    private val firstStageSummary = FirstStageSummary.getSampleFirstStageSummary(launch.flightNumber)

    @Before
    fun writeSampleData() = runBlocking {
        launchDao.save(launch)
        rocketSummaryDao.saveRocketSummary(rocketSummary)
        secondStageSummaryDao.save(secondStageSummary)
        firstStageSummaryDao.save(firstStageSummary)
    }

    @Test
    fun launchReadTest() = runBlocking {
        val launch = launchDao.getLaunch(launch.flightNumber)
        assertEquals(launch, launchDao.getLaunch(launch!!.flightNumber))
    }

    @Test
    fun nextLaunchTest() = runBlocking {
        val launch = launchDao.getNextLaunch(0L)
        assertEquals(launch, launchDao.getLaunch(launch!!.flightNumber))
    }

    @Test
    fun rocketReadTest() = runBlocking {
        val summary = rocketSummaryDao.getRocketSummary(launch.flightNumber)
        assertEquals(rocketSummary, summary)
    }

    @Test
    fun secondStageSummaryReadTest() = runBlocking {
        val summaries = secondStageSummaryDao.getAllSecondStageSummaries()
        assertEquals(1, summaries.size)
        assertEquals(secondStageSummary, summaries.first())
    }

    @Test
    fun firstStageSummaryReadTest() = runBlocking {
        val summaries = firstStageSummaryDao.getAllFirstStageSummaries()
        assertEquals(1, summaries.size)
        assertEquals(firstStageSummary, summaries.first())
    }

    @Test
    fun firstStageSummaryWithCores() = runBlocking {
        val summary = rocketSummaryDao.getFirstStage(launch.flightNumber)!!
        assertEquals(firstStageSummary, summary.firstStageSummary)
        assertTrue(summary.cores.isEmpty())
    }

    @Test
    fun secondStageSummaryWithPayloads() = runBlocking {
        val summary = rocketSummaryDao.getSecondStage(launch.flightNumber)!!
        assertEquals(secondStageSummary, summary.secondStageSummary)
        assertTrue(summary.payloads.isEmpty())
    }

    @Test
    @Ignore("Refactor this test since foreign key constraints have been removed")
    fun launchCascadedDeleteTest() = runBlocking {
        launchDao.delete(launch)

        assertTrue(launchDao.getAllLaunches(Integer.MAX_VALUE).isEmpty())

        assertTrue(rocketSummaryDao.getAllRocketSummaries().isEmpty())

        secondStageSummaryDao
            .getAllSecondStageSummaries().let {
                assertTrue(it.isEmpty())
            }

        firstStageSummaryDao
            .getAllFirstStageSummaries().let {
                assertTrue(it.isEmpty())
            }
    }
}