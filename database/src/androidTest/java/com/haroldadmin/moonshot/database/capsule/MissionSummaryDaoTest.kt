package com.haroldadmin.moonshot.database.capsule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.models.capsule.Capsule
import com.haroldadmin.moonshot.models.common.MissionSummary
import com.haroldadmin.moonshot.models.core.Core
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
internal class MissionSummaryDaoTest : BaseDbTest() {

    private val capsuleDao by lazy { db.capsuleDao() }
    private val coreDao by lazy { db.coreDao() }
    private val missionSummaryDao by lazy { db.missionSummaryDao() }

    @Test
    @Throws(Exception::class)
    fun capsuleWriteWithMissionSummariesTest() = runBlocking {
        val capsule = Capsule.getSampleCapsule()
        val core = Core.getSampleCore()
        val missions = arrayOf<MissionSummary>(
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial),
            MissionSummary.getSampleMissionSummary(capsule.serial, core.serial)
        )

        capsuleDao.saveCapsule(capsule)
        coreDao.saveCore(core)
        missionSummaryDao.saveMissionSummaries(*missions)
        capsuleDao.getCapsuleWithMissionSummaries(capsule.serial).let {
            assertEquals(capsule, it.capsule)
            assertEquals(missions.size, it.missions.size)
        }
    }

}