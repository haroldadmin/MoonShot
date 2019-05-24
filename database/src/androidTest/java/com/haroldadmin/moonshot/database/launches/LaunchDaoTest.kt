package com.haroldadmin.moonshot.database.launches

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
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
    private val secondStageSummary = SecondStageSummary.getSampleSecondStageSummary(rocketSummary.rocketId)
    private val firstStageSummary = FirstStageSummary.getSampleFirstStageSummary(rocketSummary.rocketId)

    @Before
    fun writeSampleData() = runBlocking {
        launchDao.saveLaunch(launch)
        rocketSummaryDao.saveRocketSummary(rocketSummary)
        secondStageSummaryDao.saveSecondStageSummary(secondStageSummary)
        firstStageSummaryDao.saveFirstStageSummary(firstStageSummary)
    }

    @Test
    fun launchReadTest() = runBlocking {
        val launch = launchDao.getLaunch(launch.flightNumber)
        assertEquals(launch,launchDao.getLaunch(launch.flightNumber))
    }

    @Test
    fun rocketReadTest() = runBlocking {
        val summary = rocketSummaryDao.getRocketSummary(rocketSummary.rocketId)
        assertEquals(rocketSummary, summary)
    }

    @Test
    fun secondStageSummaryReadTest() = runBlocking {
        val summaries = secondStageSummaryDao.getAllSecondStageSummaries()
        assertEquals(summaries.size, 1)
        assertEquals(summaries.first(), secondStageSummary)
    }

    @Test
    fun firstStageSummaryReadTest() = runBlocking {
        val summaries = firstStageSummaryDao.getAllFirstStageSummaries()
        assertEquals(summaries.size, 1)
        assertEquals(summaries.first(), firstStageSummary)
    }

    @Test
    fun firstStageSummaryWithCores() = runBlocking {
        val summary = rocketSummaryDao.getFirstStage(rocketSummary.rocketId)
        assertEquals(summary.firstStageSummary, firstStageSummary)
        assertTrue(summary.cores.isEmpty())
    }

    @Test
    fun secondStageSummaryWithPayloads() = runBlocking {
        val summary = rocketSummaryDao.getSecondStage(rocketSummary.rocketId)
        assertEquals(summary.secondStageSummary, secondStageSummary)
        assertTrue(summary.payloads.isEmpty())
    }

    @Test
    fun launchCascadedDeleteTest() = runBlocking {
        launchDao.deleteLaunch(launch)

        launchDao.getAllLaunches().let {
            assertTrue(it.isEmpty())
        }

        rocketSummaryDao.getAllRocketSummaries().let {
            assertTrue(it.isEmpty())
        }

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