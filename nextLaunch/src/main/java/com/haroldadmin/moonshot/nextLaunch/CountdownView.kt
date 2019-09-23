package com.haroldadmin.moonshot.nextLaunch

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.OnViewRecycled
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.utils.countdownTimer
import java.util.Timer
import java.util.concurrent.TimeUnit

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class CountdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_countdown, this)
    }

    private val countdown: AppCompatTextView = findViewById(R.id.countdownText)

    private var launchState: NextLaunchState? = null
    private var timer: Timer? = null

    @ModelProp
    fun setLaunchState(nextLaunchState: NextLaunchState) {
        this.launchState = nextLaunchState
    }

    private fun onCountdownFinish() {
        launchState?.nextLaunch?.invoke()?.let { launch ->
            this.countdown.text = if (launch.launchSuccess == true) {
                context.getString(R.string.fragmentNextLaunchCountdownSuccessfulText)
            } else {
                context.getString(R.string.fragmentNextLaunchCountdownFinishText)
            }
        }
    }

    @AfterPropsSet
    fun useProps() {
        when (val state = launchState!!.nextLaunch) {
            is Resource.Success -> {
                state.data.launchDate?.time?.let { launchTime ->
                    timer = createTimer(launchTime)
                } ?: run {
                    countdown.text = context.getString(R.string.fragmentNextLaunchCountdownUnknownText)
                }
            }

            is Resource.Error<LaunchMinimal, *> -> {
                state.data?.launchDate?.time?.let { launchTime ->
                    timer = createTimer(launchTime)
                } ?: run {
                    countdown.text = context.getString(R.string.fragmentNextLaunchCountdownErrorText)
                }
            }
        }
    }

    @OnViewRecycled
    fun clear() {
        timer?.cancel()
        timer = null
    }

    private fun calculateTimeUntilLaunch(
        timeUntilFinished: Long,
        @Suppress("SameParameterValue")
        timeUnit: TimeUnit
    ): LaunchTime {
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

        return LaunchTime(days, hours, minutes, seconds)
    }

    private fun createTimer(launchTime: Long): Timer {
        val countDownDuration = launchTime - System.currentTimeMillis()

        return countdownTimer(
            duration = countDownDuration,
            onFinish = this::onCountdownFinish
        ) { millisUntilFinished ->
            val timeText = calculateTimeUntilLaunch(millisUntilFinished, TimeUnit.MILLISECONDS)
            post {
                countdown.text = timeText.toString()
            }
        }
    }
}

private data class LaunchTime(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long
) {
    override fun toString(): String {
        return "${days}D:${hours}H:${minutes}M:${seconds}S"
    }
}