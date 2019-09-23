package com.haroldadmin.moonshot.nextLaunch

import androidx.lifecycle.viewModelScope
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.utils.countdownTimer
import com.haroldadmin.moonshotRepository.launch.GetNextLaunchUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import java.util.Timer
import java.util.concurrent.TimeUnit

class NextLaunchViewModel(
    initState: NextLaunchState,
    private val nextLaunchUseCase: GetNextLaunchUseCase
) : MoonShotViewModel<NextLaunchState>(initState) {

    private val timeAtStartOfDay: Long
        get() = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis

    init {
        viewModelScope.launch {
            getNextLaunch(timeAtStartOfDay)
        }
    }

    suspend fun getNextLaunch(timeAtStartOfDay: Long) {
        nextLaunchUseCase
            .getNextLaunch(timeAtStartOfDay)
            .collect { nextLaunchRes ->
                setState { copy(nextLaunch = nextLaunchRes) }
            }
    }
}
