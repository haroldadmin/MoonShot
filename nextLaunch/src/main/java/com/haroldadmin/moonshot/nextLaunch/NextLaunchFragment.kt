package com.haroldadmin.moonshot.nextLaunch

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLaunchDetail
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.models.LONG_DATE_FORMAT
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.nextLaunch.databinding.FragmentNextLaunchBinding
import com.haroldadmin.moonshot.utils.countdownTimer
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.vector.withState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.util.Timer
import java.util.concurrent.TimeUnit

class NextLaunchFragment : MoonShotFragment() {

    private lateinit var binding: FragmentNextLaunchBinding
    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))
    private val viewModel by viewModel<NextLaunchViewModel> {
        parametersOf(NextLaunchState())
    }
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NextLaunch.init()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNextLaunchBinding.inflate(inflater, container, false)
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvNextLaunch.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    override fun onStart() {
        super.onStart()
        if (timer == null) withState(viewModel) { state ->
            setupCountdown(state)
        }
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
        timer = null
    }

    override fun renderState() = withState(viewModel) { state ->
        setupCountdown(state)
        epoxyController.setData(state)
        if (!state.notificationScheduled) {
            fragmentScope.launch {
                if (state.nextLaunch is Resource.Success) {
                    viewModel.persistNextLaunchValues(requireContext())
                }
            }
        }
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val launch = state.nextLaunch) {

                is Resource.Success -> {
                    buildLaunchModels(this, launch.data)
                }

                is Resource.Error<LaunchMinimal, *> -> {
                    itemError {
                        id("next-launch-error")
                        error(getString(R.string.fragmentNextLaunchErrorMessage))
                    }
                    if (launch.data != null) {
                        buildLaunchModels(this, launch.data!!)
                    }
                }
                else -> itemLoading {
                    id("next-launch-loading")
                    message(getString(R.string.fragmentNextLaunchLoadingMessage))
                }
            }

            when (val countDown = state.countDown) {
                is Resource.Success ->
                    itemCountdown {
                        id("launch-countdown")
                        time(countDown.data)
                    }
                else -> Unit
            }
        }
    }

    private fun showLaunchDetails(flightNumber: Int) {
        NextLaunchFragmentDirections.launchDetails(flightNumber).let { action ->
            findNavController().navigate(action)
        }
    }

    private fun buildLaunchModels(
        controller: EpoxyController,
        launch: LaunchMinimal
    ) {
        with(controller) {
            itemLaunchCard {
                id(launch.flightNumber)
                launch(launch)
                header(getString(R.string.fragmentNextLaunchNextLaunchHeaderText))
                onLaunchClick { _ -> showLaunchDetails(launch.flightNumber) }
            }

            itemLaunchDetail {
                id("launch-date")
                detailHeader(getString(R.string.fragmentNextLaunchDateHeader))
                detailName(
                    launch.launchDate?.format(resources.configuration, LONG_DATE_FORMAT)
                        ?: getString(R.string.fragmentNextLaunchNoLaunchDateText)
                )
                detailIcon(ContextCompat.getDrawable(requireContext(), appR.drawable.ic_round_date_range_24px))
            }

            itemLaunchDetail {
                id("launch-site")
                detailHeader(getString(R.string.fragmentNextLaunchLaunchSiteHeader))
                detailName(launch.siteNameLong)
                detailIcon(ContextCompat.getDrawable(requireContext(), appR.drawable.ic_round_place_24px))
                onDetailClick { _ ->
                    launch.siteId?.let { id ->
                        val action = NextLaunchFragmentDirections.launchPadDetails(id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun setupCountdown(state: NextLaunchState) {
        if (state.nextLaunch is Resource.Success && timer == null) {
            val launchTime = state.nextLaunch.data.launchDate?.time ?: 0L
            val duration = launchTime - System.currentTimeMillis()
            timer = createCountdownTimer(duration)
        }
    }

    private fun createCountdownTimer(duration: Long): Timer {
        return countdownTimer(duration = duration, onFinish = {
            viewModel.updateCountdownTime(getString(R.string.fragmentNextLaunchCountdownFinishText))
        }) { millisUntilFinished ->
            val timeText = calculateTimeUntilLaunch(millisUntilFinished, TimeUnit.MILLISECONDS).toString()
            viewModel.updateCountdownTime(timeText)
        }
    }

    private fun calculateTimeUntilLaunch(timeUntilFinished: Long, timeUnit: TimeUnit): TimeUntilLaunch {
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
