package com.haroldadmin.moonshot_repository.launches

import com.haroldadmin.moonshot.database.launch.rocket.first_stage.FirstStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.first_stage.FirstStageWithCoreSummaries

class FakeFirstStageSummaryDao(
    private val fssId: Int = 0,
    private val rocketId: String = "falcon9"
) : FirstStageSummaryDao() {

    override suspend fun save(obj: FirstStageSummary) = Unit

    override suspend fun saveAll(vararg obj: FirstStageSummary) = Unit

    override suspend fun saveAll(objs: List<FirstStageSummary>) = Unit

    override suspend fun update(obj: FirstStageSummary) = Unit

    override suspend fun updateAll(vararg obj: FirstStageSummary) = Unit

    override suspend fun updateAll(objs: List<FirstStageSummary>) = Unit

    override suspend fun delete(obj: FirstStageSummary) = Unit

    override suspend fun deleteAll(vararg obj: FirstStageSummary) = Unit

    override suspend fun deleteAll(objs: List<FirstStageSummary>) = Unit

    override suspend fun getAllFirstStageSummaries(): List<FirstStageSummary> = listOf()

    override suspend fun getFirstStageSummary(id: Int): FirstStageSummary = FirstStageSummary.getSampleFirstStageSummary(rocketId)

    override suspend fun getCoreSummary(serial: String): CoreSummary = CoreSummary.getSampleCoreSummary(fssId)

    override suspend fun getFirstStageWithCoreSummaries(id: Int): FirstStageWithCoreSummaries = FirstStageWithCoreSummaries(
        FirstStageSummary.getSampleFirstStageSummary(rocketId),
        listOf()
    )
}