package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload

class FakeSecondStageSummaryDao(
    private val rocketId: String = "falcon9",
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

    override suspend fun getSecondStageSummary(id: Int): SecondStageSummary = SecondStageSummary.getSampleSecondStageSummary(rocketId)

    override suspend fun getPayload(id: String): Payload = Payload.getSamplePayload(sssId)

    override suspend fun getSecondStageWithPayloads(id: Int): SecondStageSummaryWithPayloads = SecondStageSummaryWithPayloads(
        SecondStageSummary.getSampleSecondStageSummary(rocketId),
        listOf()
    )
}