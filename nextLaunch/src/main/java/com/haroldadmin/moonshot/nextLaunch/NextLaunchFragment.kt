package com.haroldadmin.moonshot.nextLaunch

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.MainViewModel
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
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.vector.activityViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import com.haroldadmin.moonshot.R as appR

class NextLaunchFragment : MoonShotFragment() {

    private lateinit var binding: FragmentNextLaunchBinding
    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))
    private val viewModel by viewModel<NextLaunchViewModel> {
        parametersOf(NextLaunchState())
    }
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NextLaunch.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNextLaunchBinding.inflate(inflater, container, false)
        mainViewModel.setTitle(getString(appR.string.title_next_launch))
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvNextLaunch.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        renderState(viewModel) { state -> epoxyController.setData(state) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val launch = state.nextLaunch) {

                is Resource.Success -> {
                    buildLaunchModels(this, launch.data)
                    countdownView {
                        id("launch-countdown")
                        launchState(state)
                    }
                }

                is Resource.Error<LaunchMinimal, *> -> {
                    itemError {
                        id("next-launch-error")
                        error(getString(R.string.fragmentNextLaunchErrorMessage))
                    }
                    if (launch.data != null) {
                        buildLaunchModels(this, launch.data!!)
                        countdownView {
                            id("launch-countdown")
                            launchState(state)
                        }
                    }
                }
                else -> itemLoading {
                    id("next-launch-loading")
                    message(getString(R.string.fragmentNextLaunchLoadingMessage))
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
                detailIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        appR.drawable.ic_round_date_range_24px
                    )
                )
            }

            itemLaunchDetail {
                id("launch-site")
                detailHeader(getString(R.string.fragmentNextLaunchLaunchSiteHeader))
                detailName(launch.siteNameLong)
                detailIcon(
                    ContextCompat.getDrawable(
                        requireContext(),
                        appR.drawable.ic_round_place_24px
                    )
                )
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
