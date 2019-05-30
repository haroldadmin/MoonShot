package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries

class FakeFirstStageSummaryDao(
    private val fssId: Long = 0L,
    private val rocketId: String = "falcon9"
) : FirstStageSummaryDao {
    override suspend fun getAllFirstStageSummaries(): List<FirstStageSummary> = listOf()

    override suspend fun getFirstStageSummary(id: Int): FirstStageSummary = FirstStageSummary.getSampleFirstStageSummary(rocketId)

    override suspend fun getCoreSummary(serial: String): CoreSummary = CoreSummary.getSampleCoreSummary(fssId)

    override suspend fun getFirstStageWithCoreSummaries(id: Int): FirstStageWithCoreSummaries = FirstStageWithCoreSummaries(
        FirstStageSummary.getSampleFirstStageSummary(rocketId),
        listOf()
    )

    override suspend fun saveFirstStageSummary(firstStageSummary: FirstStageSummary): Long = fssId

    override suspend fun saveCoreSummary(coreSummary: CoreSummary) = Unit

    override suspend fun saveCoreSummaries(vararg coreSummary: CoreSummary) = Unit

    override suspend fun saveCoreSummaries(coreSummaries: List<CoreSummary>) = Unit

    override suspend fun deleteFirstStageSummary(firstStageSummary: FirstStageSummary) = Unit

    override suspend fun deleteCoreSummary(coreSummary: CoreSummary) = Unit
}