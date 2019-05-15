package com.haroldadmin.moonshot.database

import com.haroldadmin.moonshot.database.launch.Launch
import com.haroldadmin.moonshot.database.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummary
import org.junit.Before
import org.junit.Test

class LaunchDaoTest : BaseDbTest() {

    private val launchDao by lazy { db.launchDao() }
    private val rocketSummaryDao by lazy { db.rocketSummaryDao() }
    private val secondStageSummaryDao by lazy { db.secondStageSummaryDao() }
    private val firstStageSummaryDao by lazy { db.firstStageSummaryDao() }
    private val launch = Launch.getSampleLaunch()
    private val rocketSummary = RocketSummary.getSampleRocketSummary(launch.flightNumber)
    private val secondStageSummary = SecondStageSummary.getSampleSecondStageSummary(rocketSummary.rocketId)
    private val firstStageSummary = FirstStageSummary.getSampleFirstStageSummary(rocketSummary.rocketId)

    @Before
    fun writeSampleData() {
        launchDao.saveLaunch(launch).blockingAwait()
        rocketSummaryDao.saveRocketSummary(rocketSummary).blockingAwait()
        secondStageSummaryDao.saveSecondStageSummary(secondStageSummary).blockingAwait()
        firstStageSummaryDao.saveFirstStageSummary(firstStageSummary).blockingAwait()
    }

    @Test
    fun launchReadTest() {
        launchDao
            .getLaunch(launch.flightNumber)
            .test()
            .assertValue(launch)
    }

    @Test
    fun rocketReadTest() {
        rocketSummaryDao
            .getRocketSummary(rocketSummary.rocketId)
            .test()
            .assertValue(rocketSummary)
    }

    @Test
    fun secondStageSummaryReadTest() {
        secondStageSummaryDao
            .getAllSecondStageSummaries()
            .take(1)
            .test()
            .await()
            .assertValue { list ->
                list.first() == secondStageSummary
            }
    }

    @Test
    fun firstStageSummaryReadTest() {
        firstStageSummaryDao
            .getAllFirstStageSummaries()
            .take(1)
            .test()
            .await()
            .assertValue { list ->
                list.first() == firstStageSummary
            }
    }

    @Test
    fun firstStageSummaryWithCores() {
        rocketSummaryDao
            .getFirstStage(rocketSummary.rocketId)
            .test()
            .assertValue { firstStageWithCores ->
                firstStageWithCores.firstStageSummary == firstStageSummary &&
                        firstStageWithCores.cores.isEmpty()
            }
    }

    @Test
    fun secondStageSummaryWithPayloads() {
        rocketSummaryDao
            .getSecondStage(rocketSummary.rocketId)
            .test()
            .assertValue { secondStageWithPayloads ->
                secondStageWithPayloads.secondStageSummary == secondStageSummary &&
                        secondStageWithPayloads.payloads.isEmpty()
            }
    }

    @Test
    fun launchCascadedDeleteTest() {
        launchDao.deleteLaunch(launch).blockingAwait()

        launchDao
            .getAllLaunches()
            .test()
            .assertValue {
                it.isEmpty()
            }

        rocketSummaryDao
            .getAllRocketSummaries()
            .test()
            .assertValue {
                it.isEmpty()
            }

        secondStageSummaryDao
            .getAllSecondStageSummaries()
            .test()
            .assertValue {
                it.isEmpty()
            }

        firstStageSummaryDao
            .getAllFirstStageSummaries()
            .test()
            .assertValue {
                it.isEmpty()
            }
    }
}