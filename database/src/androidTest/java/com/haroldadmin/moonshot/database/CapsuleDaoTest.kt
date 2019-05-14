package com.haroldadmin.moonshot.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.database.capsule.Capsule
import com.haroldadmin.moonshot.database.capsule.CapsuleDao
import com.haroldadmin.moonshot.database.common.MissionSummary
import com.haroldadmin.moonshot.database.common.MissionSummaryDao
import org.junit.After
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
class CapsuleDaoTest: KoinTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val db: MoonShotDb by inject()
    private val capsuleDao: CapsuleDao by lazy { db.capsuleDao() }
    private val missionSummaryDao: MissionSummaryDao by lazy { db.missionSummaryDao() }

    @Before
    fun setup() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(androidTestModule)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
        stopKoin()
    }

    @Test
    @Throws(Exception::class)
    fun capsuleWriteTest() {
        val capsule = Capsule.getSampleCapsule()
        with(capsuleDao) {
            saveCapsule(capsule).blockingAwait()
            getCapsule(capsule.serial)
                .test()
                .assertValue(capsule)
        }
    }
}