package com.haroldadmin.moonshotRepository.mission

import com.haroldadmin.moonshot.database.MissionDao
import com.haroldadmin.moonshot.models.Mission
import com.haroldadmin.moonshotRepository.FakeStatefulDao

internal class FakeMissionDao : MissionDao(), FakeStatefulDao<Mission> {

    override val items: MutableList<Mission> = mutableListOf()

    override fun forId(missionId: String): Mission? {
        return items.find { it.id == missionId }
    }

    override fun all(limit: Int, offset: Int): List<Mission> {
        return items.take(limit).drop(offset).sortedBy { it.name }
    }

    override suspend fun clearTable() {
        items.clear()
    }
}