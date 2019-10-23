package com.haroldadmin.moonshot.nextLaunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.models.DatePrecision
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.nextLaunch.databinding.FragmentNextLaunchBinding
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.detailCard
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.vector.fragmentViewModel
import com.haroldadmin.moonshot.R as appR

class NextLaunchFragment : ComplexMoonShotFragment<NextLaunchViewModel, NextLaunchState>() {

    private lateinit var binding: FragmentNextLaunchBinding

    private val mainViewModel: MainViewModel by activityViewModel()
    override val viewModel: NextLaunchViewModel by fragmentViewModel()

    override fun initDI() = NextLaunch.init()

    override fun renderer(state: NextLaunchState) {
        epoxyController.setData(state)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNextLaunchBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_next_launch))

        binding.rvNextLaunch.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }
        return binding.root
    }

    override val epoxyController by lazy {
        asyncTypedEpoxyController(viewModel) { state ->
            when (val launch = state.nextLaunch) {

                is Resource.Success -> {
                    buildLaunchModels(this, launch.data)
                    if (launch.data.maxPrecision == DatePrecision.hour) {
                        countdownView {
                            id("launch-countdown")
                            launchState(state)
                        }
                    }
                }

                is Resource.Error<LaunchMinimal, *> -> {
                    errorView {
                        id("next-launch-error")
                        errorText(getString(R.string.fragmentNextLaunchErrorMessage))
                    }
                    if (launch.data != null) {
                        buildLaunchModels(this, launch.data!!)
                        if (launch.data!!.maxPrecision == DatePrecision.hour) {
                            countdownView {
                                id("launch-countdown")
                                launchState(state)
                            }
                        }
                    }
                }
                else -> loadingView {
                    id("next-launch-loading")
                    loadingText(getString(R.string.fragmentNextLaunchLoadingMessage))
                }
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
            launchCard {
                id(launch.flightNumber)
                launch(launch)
                header(getString(R.string.fragmentNextLaunchNextLaunchHeaderText))
                onLaunchClick { _ -> showLaunchDetails(launch.flightNumber) }
            }

            detailCard {
                id("launch-date")
                header(getString(R.string.fragmentNextLaunchDateHeader))
                content(launch.launchDateText)
                icon(appR.drawable.ic_round_date_range_24px)
            }

            detailCard {
                id("launch-site")
                header(getString(R.string.fragmentNextLaunchLaunchSiteHeader))
                content(launch.siteNameLong ?: getString(R.string.siteUnknownText))
                icon(appR.drawable.ic_round_place_24px)
                onDetailClick { _ ->
                    launch.siteId?.let { id ->
                        val action = NextLaunchFragmentDirections.launchPadDetails(id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}
