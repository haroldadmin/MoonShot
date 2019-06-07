package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummaryWithPayloads

class FakeRocketSummariesDao(
    private val flightNumber: Int = 0
) : RocketSummaryDao {
    override suspend fun getRocketSummaryForLaunch(flightNumber: Int): RocketSummary {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllRocketSummaries(): List<RocketSummary> = listOf()

    override suspend fun getFirstStage(flightNumber: Int): FirstStageWithCoreSummaries = FirstStageWithCoreSummaries(
        FirstStageSummary.getSampleFirstStageSummary(this.flightNumber),
        listOf()
    )

    override suspend fun getSecondStage(flightNumber: Int): SecondStageSummaryWithPayloads = SecondStageSummaryWithPayloads(
        SecondStageSummary.getSampleSecondStageSummary(this.flightNumber),
        listOf()
    )

    override suspend fun saveRocketSummary(rocketSummary: RocketSummary) = Unit

    override suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>) = Unit

    override suspend fun deleteRocketSummary(rocketSummary: RocketSummary) = Unit
}