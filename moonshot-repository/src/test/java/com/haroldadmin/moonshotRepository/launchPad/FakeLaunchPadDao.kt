package com.haroldadmin.moonshotRepository.launchPad

import com.haroldadmin.moonshot.database.LaunchPadDao
import com.haroldadmin.moonshot.models.LaunchPad
import com.haroldadmin.moonshot.models.SampleDbData

internal class FakeLaunchPadDao : LaunchPadDao() {

    override suspend fun one(siteId: String): LaunchPad? {
        return SampleDbData.LaunchPads.one(siteId = siteId)
    }

    override suspend fun all(limit: Int, offset: Int): List<LaunchPad> {
        return SampleDbData.LaunchPads.many().take(limit).toList()
    }

    override suspend fun forQuery(query: String, limit: Int, offset: Int): List<LaunchPad> {
        return SampleDbData.LaunchPads
            .many(siteNameGenerator = { query })
            .take(limit)
            .toList()
    }

    override suspend fun clearTable() = Unit

    override suspend fun save(obj: LaunchPad) = Unit

    override suspend fun saveAll(objs: List<LaunchPad>) = Unit

    override suspend fun update(obj: LaunchPad) = Unit

    override suspend fun updateAll(objs: List<LaunchPad>) = Unit

    override suspend fun delete(obj: LaunchPad) = Unit

    override suspend fun deleteAll(objs: List<LaunchPad>) = Unit

}