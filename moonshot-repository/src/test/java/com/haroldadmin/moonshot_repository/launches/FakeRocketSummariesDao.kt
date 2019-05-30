package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.rocket.RocketSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.RocketSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads

class FakeRocketSummariesDao(
    private val flightNumber: Int = 0,
    private val rocketId: String = "falcon9"
) : RocketSummaryDao {
    override suspend fun getAllRocketSummaries(): List<RocketSummary> = listOf()

    override suspend fun getRocketSummary(id: String): RocketSummary = RocketSummary.getSampleRocketSummary(flightNumber)

    override suspend fun getFirstStage(id: String): FirstStageWithCoreSummaries = FirstStageWithCoreSummaries(
        FirstStageSummary.getSampleFirstStageSummary(rocketId),
        listOf()
    )

    override suspend fun getSecondStage(id: String): SecondStageSummaryWithPayloads = SecondStageSummaryWithPayloads(
        SecondStageSummary.getSampleSecondStageSummary(rocketId),
        listOf()
    )

    override suspend fun saveRocketSummary(rocketSummary: RocketSummary) = Unit

    override suspend fun saveRocketSummaries(rocketSummaries: List<RocketSummary>) = Unit

    override suspend fun deleteRocketSummary(rocketSummary: RocketSummary) = Unit
}