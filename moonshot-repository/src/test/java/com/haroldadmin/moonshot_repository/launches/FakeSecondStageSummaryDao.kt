package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.rocket.second_stage.SecondStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.SecondStageSummaryWithPayloads
import com.haroldadmin.moonshot.models.launch.rocket.second_stage.payload.Payload

class FakeSecondStageSummaryDao(
    private val rocketId: String = "falcon9",
    private val sssId: Long = 0L
) : SecondStageSummaryDao {

    override suspend fun getAllSecondStageSummaries(): List<SecondStageSummary> = listOf()

    override suspend fun getSecondStageSummary(id: Int): SecondStageSummary = SecondStageSummary.getSampleSecondStageSummary(rocketId)

    override suspend fun getPayload(id: String): Payload = Payload.getSamplePayload(sssId)

    override suspend fun getSecondStageWithPayloads(id: Int): SecondStageSummaryWithPayloads = SecondStageSummaryWithPayloads(
        SecondStageSummary.getSampleSecondStageSummary(rocketId),
        listOf()
    )

    override suspend fun saveSecondStageSummary(secondStageSummary: SecondStageSummary): Long = sssId

    override suspend fun savePayload(payload: Payload) = Unit

    override suspend fun savePayloads(payloads: List<Payload>) = Unit

    override suspend fun deleteSecondStageSummary(secondStageSummary: SecondStageSummary) = Unit

    override suspend fun deletePayload(payload: Payload) = Unit
}