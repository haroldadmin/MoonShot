package com.haroldadmin.moonshot.base

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.vector.withState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

private data class TestState(val count: Resource<Int> = Resource.Uninitialized) : MoonShotState

private class TestViewModel : MoonShotViewModel<TestState>(TestState()) {

    suspend fun updateCount() {
        executeAsResource({ copy(count = it) }) {
            Resource.Success(1)
        }
    }
}

@RunWith(AndroidJUnit4::class)
class ExecuteAsResourceTest {

    private val vm = TestViewModel()

    @Test
    fun executeAsResourceTest() = runBlocking<Unit> {
        vm.updateCount()
        withState(vm) { state ->
            assertTrue(state.count is Resource.Success)
            state.count as Resource.Success
            assertEquals(1, state.count.data)
        }
    }
}