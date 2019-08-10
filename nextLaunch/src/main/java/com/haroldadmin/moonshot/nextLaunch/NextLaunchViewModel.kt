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

    private var timer: Timer? = null
    private val timeAtStartOfDay: Long
        get() = LocalDate.now(DateTimeZone.UTC).toDateTimeAtStartOfDay().millis

    init {
        viewModelScope.launch {
            getNextLaunch(timeAtStartOfDay)
            state
                .onEach { state ->
                    if (!isActive || timer != null) {
                        return@onEach
                    }

                    when (val launch = state.nextLaunch) {
                        is Resource.Success -> {
                            val launchTime = launch.data.launchDate?.time
                            if (launchTime == null) {
                                setState { copy(countDown = "Unknown") }
                            } else {
                                timer = createTimer(launchTime)
                            }
                        }
                        is Resource.Error<LaunchMinimal, *> -> {
                            val launchTime = launch.data?.launchDate?.time
                            if (launchTime != null) {
                                timer = createTimer(launchTime)
                            } else {
                                setState { copy(countDown = "Error") }
                            }
                        }
                        else -> Unit
                    }
                }
                .collect()
        }
    }

    private suspend fun getNextLaunch(timeAtStartOfDay: Long) {
        nextLaunchUseCase
            .getNextLaunch(timeAtStartOfDay)
            .collect { nextLaunchRes ->
                setState { copy(nextLaunch = nextLaunchRes) }
            }
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun createTimer(launchTime: Long): Timer {
        val countDownDuration = launchTime - System.currentTimeMillis()

        return countdownTimer(
            duration = countDownDuration,
            onFinish = this::onCountDownFinish
        ) { millisUntilFinished ->
            val timeText = calculateTimeUntilLaunch(millisUntilFinished, TimeUnit.MILLISECONDS)
            setState {
                copy(countDown = timeText.toString())
            }
        }
    }

    private fun onCountDownFinish() = withState { state ->
        state.nextLaunch()?.let { launch ->
            if (launch.launchSuccess == true)
                setState { copy(countDown = "Launch Successful") }
            else
                setState { copy(countDown = "Happening Now") }
        }
    }

    private fun calculateTimeUntilLaunch(
        timeUntilFinished: Long,
        timeUnit: TimeUnit
    ): TimeUntilLaunch {
        var delta = timeUntilFinished
        val days = timeUnit.toDays(delta)
        if (days > 0) {
            delta %= days * timeUnit.convert(1, TimeUnit.DAYS)
        }
        val hours = timeUnit.toHours(delta)
        if (hours > 0) {
            delta %= hours * timeUnit.convert(1, TimeUnit.HOURS)
        }
        val minutes = timeUnit.toMinutes(delta)
        if (minutes > 0) {
            delta %= minutes * timeUnit.convert(1, TimeUnit.MINUTES)
        }
        val seconds = timeUnit.toSeconds(delta)

        return TimeUntilLaunch(days, hours, minutes, seconds)
    }
}

private data class TimeUntilLaunch(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) {
    override fun toString(): String {
        return "${days}D:${hours}H:${minutes}M:${seconds}S"
    }
}
