package com.haroldadmin.moonshot.nextLaunch

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.MoonShot
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.LONG_DATE_FORMAT
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate

class NextLaunchViewModel(
    initState: NextLaunchState,
    private val launchesRepository: LaunchesRepository,
    private val launchNotificationManager: LaunchNotificationManager
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            val timeAtStartOfDay = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
            getNextLaunch(timeAtStartOfDay)
        }
    }

    suspend fun getNextLaunch(currentTime: Long) =
        launchesRepository.flowNextLaunch(currentTime)
            .collect { setState { copy(nextLaunch = it)} }

    suspend fun persistNextLaunchValues(context: Context) = withContext(Dispatchers.Default) {
        withState { state ->
            if (state.nextLaunch !is Resource.Success) return@withState

            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            if (!settings.getBoolean(LaunchNotificationManager.KEY_LAUNCH_NOTIFICATIONS, true)) {
                return@withState
            }

            val preferences = context.getSharedPreferences(MoonShot.MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)

            preferences.edit(commit = true) {
                putInt(LaunchNotificationManager.KEY_FLIGHT_NUMBER, state.nextLaunch.data.flightNumber)
                putString(LaunchNotificationManager.KEY_LAUNCH_NAME, state.nextLaunch.data.missionName)
                putString(LaunchNotificationManager.KEY_LAUNCH_SITE, state.nextLaunch.data.siteName)
                putString(
                    LaunchNotificationManager.KEY_LAUNCH_DATE,
                    state.nextLaunch.data.launchDate?.format(
                        context.resources.configuration,
                        LONG_DATE_FORMAT
                    )
                )
                state.nextLaunch.data.launchDate?.let {
                    putLong(LaunchNotificationManager.KEY_LAUNCH_TIME, it.time)
                }
            }

            scheduleNotification()
            setState { copy(notificationScheduled = true) }
        }
    }

    private suspend fun scheduleNotification() = withContext(Dispatchers.Default) {
        withState { state ->
            if (state.nextLaunch !is Resource.Success) return@withState
            launchNotificationManager.scheduleNotifications()
        }
    }

    fun updateCountdownTime(timeText: String) = setState {
        copy(countDown = Resource.Success(timeText))
    }
}