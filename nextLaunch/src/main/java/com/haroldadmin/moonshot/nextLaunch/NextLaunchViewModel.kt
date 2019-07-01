package com.haroldadmin.moonshot.nextLaunch

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.MoonShot
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.notifications.LaunchNotificationsManager
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
    private val launchNotificationManager: LaunchNotificationsManager
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            val timeAtStartOfDay = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis
            getNextLaunch(timeAtStartOfDay)
        }
    }

    suspend fun getNextLaunch(currentTime: Long) =
        launchesRepository.flowNextLaunch(currentTime)
            .collect { setState { copy(nextLaunch = it) } }

    suspend fun persistNextLaunchValues(context: Context) = withContext(Dispatchers.Default) {
        withState { state ->
            if (state.nextLaunch !is Resource.Success) return@withState

            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            if (!settings.getBoolean(LaunchNotificationsManager.KEY_JUST_BEFORE_LAUNCH_NOTIFICATIONS_SETTING, true)) {
                return@withState
            }

            val preferences = context.getSharedPreferences(MoonShot.MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)

//            preferences.edit(commit = true) {
//                putInt(LaunchNotificationsManager.KEY_FLIGHT_NUMBER, state.nextLaunch.data.flightNumber)
//                putString(LaunchNotificationsManager.KEY_LAUNCH_NAME, state.nextLaunch.data.missionName)
//                putString(LaunchNotificationsManager.KEY_LAUNCH_SITE, state.nextLaunch.data.siteName)
//                putString(
//                    LaunchNotificationsManager.KEY_LAUNCH_DATE,
//                    state.nextLaunch.data.launchDate?.format(
//                        context.resources.configuration,
//                        LONG_DATE_FORMAT
//                    )
//                )
//                state.nextLaunch.data.launchDate?.let {
//                    putLong(LaunchNotificationsManager.KEY_LAUNCH_TIME, it.time)
//                }
//            }

            scheduleNotification()
            setState { copy(notificationScheduled = true) }
        }
    }

    private suspend fun scheduleNotification() = withContext(Dispatchers.Default) {
        withState { state ->
            if (state.nextLaunch !is Resource.Success) return@withState
//            launchNotificationManager.scheduleNotifications()
        }
    }

    fun updateCountdownTime(timeText: String) = setState {
        copy(countDown = Resource.Success(timeText))
    }
}