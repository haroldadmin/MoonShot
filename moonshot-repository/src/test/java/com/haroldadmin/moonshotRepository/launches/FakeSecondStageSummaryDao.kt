package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.database.launch.rocket.secondStage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.SecondStageSummaryWithPayloads
import com.haroldadmin.moonshot.models.launch.rocket.secondStage.payload.Payload

class FakeSecondStageSummaryDao(
    private val flightNumber: Int = 0,
    private val sssId: Int = 0
) : SecondStageSummaryDao() {

    override suspend fun save(obj: SecondStageSummary) = Unit

    override suspend fun saveAll(vararg obj: SecondStageSummary) = Unit

    override suspend fun saveAll(objs: List<SecondStageSummary>) = Unit

    override suspend fun update(obj: SecondStageSummary) = Unit

    override suspend fun updateAll(vararg obj: SecondStageSummary) = Unit

    override suspend fun updateAll(objs: List<SecondStageSummary>) = Unit

    override suspend fun delete(obj: SecondStageSummary) = Unit

    override suspend fun deleteAll(vararg obj: SecondStageSummary) = Unit

    override suspend fun deleteAll(objs: List<SecondStageSummary>) = Unit

    override suspend fun getAllSecondStageSummaries(): List<SecondStageSummary> = listOf()

    override suspend fun getSecondStageSummary(flightNumber: Int): SecondStageSummary =
        SecondStageSummary.getSampleSecondStageSummary(
            this.flightNumber
        )

    override suspend fun getPayload(id: String): Payload = Payload.getSamplePayload(sssId)

    override suspend fun getSecondStageWithPayloads(flightNumber: Int): SecondStageSummaryWithPayloads =
        SecondStageSummaryWithPayloads(
            SecondStageSummary.getSampleSecondStageSummary(this.flightNumber),
            listOf()
        )
}