package com.haroldadmin.moonshot.database.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.capsule.Capsule
import com.haroldadmin.moonshot.models.common.MissionSummary
import com.haroldadmin.moonshot.models.core.Core
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CoreDaoTest : BaseDbTest() {

    private val dao by lazy { db.coreDao() }
    private val capsuleDao by lazy { db.capsuleDao() }
    private val missionSummariesDao by lazy { db.missionSummaryDao() }

    @Test
    fun coreReadWriteTest() = runBlocking {
        val core = Core.getSampleCore()
        dao.saveCore(core)

        val savedCore = dao.getCore(core.serial)
        assertEquals(core, savedCore)
    }

    @Test
    fun coreMissionSummariesTest() = runBlocking {
        val core = Core.getSampleCore()
        dao.saveCore(core)

        val capsule = Capsule.getSampleCapsule()
        capsuleDao.saveCapsule(capsule)

        val missionSummaries = arrayOf(
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial)
        )
        missionSummariesDao.saveMissionSummaries(*missionSummaries)

        val coreWithMissionSummaries = dao.getCoreWtihMissionSummaries(core.serial)

        assertEquals(coreWithMissionSummaries.missions, missionSummaries.toList())
    }
}