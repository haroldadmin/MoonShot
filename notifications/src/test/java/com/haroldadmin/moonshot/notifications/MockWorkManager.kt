package com.haroldadmin.moonshot.notifications

import android.app.PendingIntent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkContinuation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.impl.utils.futures.SettableFuture
import com.google.common.util.concurrent.ListenableFuture
import com.haroldadmin.moonshot.core.append
import java.util.UUID

/**
 * A Mock for WorkManager implementing partial functionality.
 *
 * The work requests enqueued to this mock are never run. Enqueueing and cancellation operations are
 * supported.
 */
internal class MockWorkManager : WorkManager() {

    val workRequests = mutableListOf<WorkRequest>()

    private val mockOperation = object : Operation {
        override fun getState(): LiveData<Operation.State> {
            return MutableLiveData()
        }

        override fun getResult(): ListenableFuture<Operation.State.SUCCESS> {
            return SettableFuture.create()
        }
    }

    override fun enqueue(requests: MutableList<out WorkRequest>): Operation {
        workRequests.addAll(requests)
        return mockOperation
    }

    override fun cancelAllWorkByTag(tag: String): Operation {
        workRequests.removeAll { it.tags.contains(tag) }
        return mockOperation
    }

    override fun beginWith(work: MutableList<OneTimeWorkRequest>): WorkContinuation {
        TODO("Not yet implemented")
    }

    override fun enqueueUniqueWork(
        uniqueWorkName: String,
        existingWorkPolicy: ExistingWorkPolicy,
        work: MutableList<OneTimeWorkRequest>
    ): Operation {
        workRequests.addAll(work)
        return mockOperation
    }

    override fun getWorkInfoById(id: UUID): ListenableFuture<WorkInfo> {
        TODO()
    }

    override fun getLastCancelAllTimeMillisLiveData(): LiveData<Long> {
        TODO("Not yet implemented")
    }

    override fun cancelAllWork(): Operation {
        workRequests.clear()
        return mockOperation
    }

    override fun getWorkInfosForUniqueWorkLiveData(uniqueWorkName: String): LiveData<MutableList<WorkInfo>> {
        TODO("Not yet implemented")
    }

    override fun createCancelPendingIntent(id: UUID): PendingIntent {
        TODO("Not yet implemented")
    }

    override fun pruneWork(): Operation {
        TODO("Not yet implemented")
    }

    override fun beginUniqueWork(
        uniqueWorkName: String,
        existingWorkPolicy: ExistingWorkPolicy,
        work: MutableList<OneTimeWorkRequest>
    ): WorkContinuation {
        TODO("Not yet implemented")
    }

    override fun cancelUniqueWork(uniqueWorkName: String): Operation {
        workRequests.removeAll { it.workSpec.workerClassName == uniqueWorkName }
        return mockOperation
    }

    override fun enqueueUniquePeriodicWork(
        uniqueWorkName: String,
        existingPeriodicWorkPolicy: ExistingPeriodicWorkPolicy,
        periodicWork: PeriodicWorkRequest
    ): Operation {
        workRequests.add(periodicWork)
        return mockOperation
    }

    override fun getLastCancelAllTimeMillis(): ListenableFuture<Long> {
        TODO("Not yet implemented")
    }

    override fun getWorkInfoByIdLiveData(id: UUID): LiveData<WorkInfo> {
        TODO("Not yet implemented")
    }

    override fun getWorkInfosByTag(tag: String): ListenableFuture<MutableList<WorkInfo>> {
        TODO("Not yet implemented")
    }

    override fun getWorkInfosForUniqueWork(uniqueWorkName: String): ListenableFuture<MutableList<WorkInfo>> {
        TODO("Not yet implemented")
    }

    override fun cancelWorkById(id: UUID): Operation {
        workRequests.removeIf { it.id == id }
        return mockOperation
    }

    override fun getWorkInfosByTagLiveData(tag: String): LiveData<MutableList<WorkInfo>> {
        TODO("Not yet implemented")
    }
}