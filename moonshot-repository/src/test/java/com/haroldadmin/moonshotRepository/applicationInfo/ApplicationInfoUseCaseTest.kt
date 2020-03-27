package com.haroldadmin.moonshotRepository.applicationInfo

import com.haroldadmin.moonshot.core.TestDispatchers
import com.haroldadmin.moonshot.models.ApplicationInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.util.Date

@ExperimentalCoroutinesApi
internal class ApplicationInfoUseCaseTest {

    private val dao = FakeApplicationInfoDao()
    private val usecase = ApplicationInfoUseCase(dao, TestDispatchers())

    @Test
    fun `should return application info when requested`() = runBlocking {
        val expected = dao.applicationInfo()
        val actual = usecase.getApplicationInfo()

        assertEquals(expected, actual)
    }

    @Test
    fun `should save application info when saving`() = runBlocking {
        val initial = dao.applicationInfo()

        ApplicationInfo(Date().time).also {
            usecase.save(it)
        }

        val new = dao.applicationInfo()

        assertNotEquals(initial, new)
    }

    @Test
    fun `should update info when updating`() = runBlocking {
        val initial = dao.applicationInfo()

        ApplicationInfo(Date().time).also {
            usecase.update(it)
        }

        val new = dao.applicationInfo()

        assertNotEquals(initial, new)
    }
}
