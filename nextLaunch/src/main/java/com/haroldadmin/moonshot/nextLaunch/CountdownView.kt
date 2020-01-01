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
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.utils.countdownTimer
import java.util.Date
import java.util.Timer
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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

    private var launchRes: Resource<Launch> = Resource.Uninitialized
    private var timer: Timer? = null

    @ModelProp
    fun setLaunchResource(launchRes: Resource<Launch>) {
        this.launchRes = launchRes
    }

    @AfterPropsSet
    fun useProps() {

        timer?.cancel()

        when (val launch = launchRes) {

            is Resource.Success -> setupCountdown(launch())

            is Resource.Error<*, *> -> launch()?.let {
                setupCountdown(it)
            } ?: run {
                countdown.text = context.getString(R.string.fragmentNextLaunchCountdownErrorText)
            }
            else -> Unit
        }
    }

    @OnViewRecycled
    fun clear() {
        timer?.cancel()
        timer = null
    }

    private fun setupCountdown(launch: Launch) = with(launch) {
        when (tentativeMaxPrecision) {
            DatePrecision.hour -> {
                timer = createTimer(
                    launchTime = launchDateUtc.time,
                    onTick = { timeToFinish ->
                        val timeText = createCountdownText(timeToFinish, TimeUnit.MILLISECONDS)
                        post { countdown.text = timeText }
                    },
                    onCountDownFinish = {
                        post {
                            countdown.text = if (launchSuccess == true) {
                                context.getString(R.string.fragmentNextLaunchCountdownSuccessfulText)
                            } else {
                                context.getString(R.string.fragmentNextLaunchCountdownFinishText)
                            }
                        }
                    }
                )
            }
            DatePrecision.day -> {
                val days = launchDateUtc.differenceInDays(Date())
                val plural = resources.getQuantityString(R.plurals.day, days.toInt(), days.toInt())
                countdown.text = plural
            }
            else -> {
                countdown.text = context.getString(R.string.fragmentNextLaunchCountdownUncertainText)
            }
        }
    }

    private fun createCountdownText(
        timeUntilFinished: Long,
        @Suppress("SameParameterValue")
        timeUnit: TimeUnit
    ): String {
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

        return "${days}D:${hours}H:${minutes}M:${seconds}S"
    }

    private fun createTimer(launchTime: Long, onTick: (Long) -> Unit, onCountDownFinish: () -> Unit): Timer {
        val countDownDuration = launchTime - System.currentTimeMillis()
        return countdownTimer(
            duration = countDownDuration,
            onFinish = onCountDownFinish,
            action = onTick
        )
    }

    private fun Date.differenceInDays(other: Date): Long {
        return abs(time - other.time) / (86400 * 1000)
    }
}
