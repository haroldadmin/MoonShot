package com.haroldadmin.moonshot.notifications.workers

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.last
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.notifications.DayBeforeLaunch
import com.haroldadmin.moonshot.notifications.JustBeforeLaunch
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import org.joda.time.DateTime

class ScheduleWorker(
    appContext: Context,
    params: WorkerParameters,
    private val nextLaunchUseCase: GetNextLaunchUseCase,
    private val launchNotificationsManager: LaunchNotificationsManager
) : CoroutineWorker(appContext, params) {

    companion object { const val name = "sync-worker" }

    @ExperimentalCoroutinesApi
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val now = DateTime()
        val dayFromNow = now.plusDays(1).millis

        val nextLaunch = nextLaunchUseCase.getNextLaunch().last()
        val launchesUntilTomorrow = nextLaunchUseCase.getNextLaunchesUntilDate(dayFromNow).last()

        if (nextLaunch !is Resource.Success<Launch> || launchesUntilTomorrow !is Resource.Success<List<Launch>>) {
            Result.retry()
        } else {
            scheduleNotifications(nextLaunch(), launchesUntilTomorrow())
            Result.success()
        }
    }

    private fun scheduleNotifications(
        nextLaunch: Launch,
        launchesUntilTomorrow: List<Launch>
    ) {
        val settings = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        if (settings.getBoolean(JustBeforeLaunch().enabledPreferenceKey, true)) {
            launchNotificationsManager.schedule(JustBeforeLaunch(), nextLaunch.launchDateUtc.time)
        }

        if (settings.getBoolean(DayBeforeLaunch().enabledPreferenceKey, true)) {
            launchesUntilTomorrow.forEach {
                launchNotificationsManager.schedule(DayBeforeLaunch(), it.launchDateUtc.time)
            }
        }
    }
}