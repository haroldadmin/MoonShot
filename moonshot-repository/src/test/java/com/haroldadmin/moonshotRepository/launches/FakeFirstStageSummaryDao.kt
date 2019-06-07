package com.haroldadmin.moonshotRepository.launches

import com.haroldadmin.moonshot.database.launch.rocket.firstStage.FirstStageSummaryDao
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.CoreSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageSummary
import com.haroldadmin.moonshot.models.launch.rocket.firstStage.FirstStageWithCoreSummaries

class FakeFirstStageSummaryDao(
    private val fssId: Int = 0,
    private val flightNumber: Int = 65
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

    override suspend fun getFirstStageSummary(flightNumber: Int): FirstStageSummary = FirstStageSummary.getSampleFirstStageSummary(
        this.flightNumber
    )

    override suspend fun getCoreSummary(serial: String): CoreSummary = CoreSummary.getSampleCoreSummary(fssId)

    override suspend fun getFirstStageWithCoreSummaries(flightNumber: Int): FirstStageWithCoreSummaries = FirstStageWithCoreSummaries(
        FirstStageSummary.getSampleFirstStageSummary(this.flightNumber),
        listOf()
    )
}