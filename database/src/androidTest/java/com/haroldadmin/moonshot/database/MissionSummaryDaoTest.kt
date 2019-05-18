package com.haroldadmin.moonshot.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.capsule.Capsule
import com.haroldadmin.moonshot.database.common.MissionSummary
import com.haroldadmin.moonshot.database.core.Core
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class MissionSummaryDaoTest: BaseDbTest() {

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