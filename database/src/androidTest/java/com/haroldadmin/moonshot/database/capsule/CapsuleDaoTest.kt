package com.haroldadmin.moonshot.database.capsule

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.BaseDbTest
import com.haroldadmin.moonshot.database.common.MissionSummaryDao
import com.haroldadmin.moonshot.models.capsule.Capsule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
internal class CapsuleDaoTest : BaseDbTest() {

    private val capsuleDao: CapsuleDao by lazy { db.capsuleDao() }
    private val missionSummaryDao: MissionSummaryDao by lazy { db.missionSummaryDao() }

    @Test
    @Throws(Exception::class)
    fun capsuleWriteTest() = runBlocking {
        val capsule = Capsule.getSampleCapsule()
        with(capsuleDao) {
            saveCapsule(capsule)
            assertEquals(capsule, getCapsule(capsule.serial))
        }
    }
}