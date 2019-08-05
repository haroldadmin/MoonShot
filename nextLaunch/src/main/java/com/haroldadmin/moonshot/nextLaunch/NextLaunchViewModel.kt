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
    private val launchesRepository: LaunchesRepository
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

    fun updateCountdownTime(timeText: String) = setState {
        copy(countDown = Resource.Success(timeText))
    }
}