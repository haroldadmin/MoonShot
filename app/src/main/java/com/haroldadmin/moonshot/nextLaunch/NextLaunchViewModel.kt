package com.haroldadmin.moonshot.nextLaunch

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_NAME
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_NOTIFICATIONS
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_SITE
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_DATE
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_TIME
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import com.haroldadmin.moonshot.notifications.MOONSHOT_SHARED_PREFS
import com.haroldadmin.moonshot.notifications.NOTIFICATION_DATE_FORMAT
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.LaunchesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class NextLaunchViewModel(
    initState: NextLaunchState,
    private val launchesRepository: LaunchesRepository,
    private val launchNotificationManager: LaunchNotificationManager
) : MoonShotViewModel<NextLaunchState>(initState) {

    init {
        viewModelScope.launch {
            getNextLaunch(Calendar.getInstance().timeInMillis)
        }
    }

    suspend fun getNextLaunch(currentTime: Long) =
        launchesRepository.flowNextLaunch(currentTime)
            .collect { setState { copy(nextLaunch = it) } }

    suspend fun persistNextLaunchValues(context: Context) = withContext(Dispatchers.Default) {
        withState { state ->

            if (state.nextLaunch !is Resource.Success) return@withState

            val settings = PreferenceManager.getDefaultSharedPreferences(context)
            if (!settings.getBoolean(KEY_LAUNCH_NOTIFICATIONS, true)) {
                return@withState
            }

            val preferences = context.getSharedPreferences(MOONSHOT_SHARED_PREFS, Context.MODE_PRIVATE)

            preferences.edit(commit = true) {
                putString(KEY_LAUNCH_NAME, state.nextLaunch.data.missionName)
                putString(KEY_LAUNCH_SITE, state.nextLaunch.data.siteName)
                putString(
                    KEY_LAUNCH_DATE,
                    state.nextLaunch.data.launchDate?.format(context.resources.configuration, NOTIFICATION_DATE_FORMAT)
                )
                state.nextLaunch.data.launchDate?.let {
                    putLong(KEY_LAUNCH_TIME, it.time)
                }
            }

            scheduleNotification()
        }
    }

    private suspend fun scheduleNotification() = withContext(Dispatchers.Default) {
        withState { state ->
            if (state.nextLaunch !is Resource.Success) return@withState
            launchNotificationManager.scheduleNotifications()
        }
    }
}