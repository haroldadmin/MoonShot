package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.haroldadmin.moonshot.LaunchTypes
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.launches.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.utils.log
import com.haroldadmin.moonshotRepository.launch.LaunchesFilter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.qualifier.named

class LaunchesFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchesBinding
    private val safeArgs by navArgs<LaunchesFragmentArgs>()
    private val viewModel by navGraphViewModels<LaunchesViewModel>(appR.id.launchesFlow) {
        LaunchesViewModelFactory(LaunchesState(
            type = safeArgs.type,
            siteId = safeArgs.siteId
        ))
    }
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val diffingHandler by inject<Handler>(named("differ"))
    private val buildingHandler by inject<Handler>(named("builder"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Launches.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        val animation =
            AnimationUtils.loadLayoutAnimation(requireContext(), appR.anim.layout_animation_fade_in)
        binding.rvLaunches.apply {
            setController(epoxyController)
            layoutAnimation = animation
        }
        binding.fabFilter.apply {
            setOnClickListener {
                findNavController().navigate(LaunchesFragmentDirections.showFilters())
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentScope.launch {
            viewModel.state.collect {
                renderState(it) { state ->
                    epoxyController.setData(state)
                    if (state.siteName != null) {
                        mainViewModel.setTitle(state.siteName)
                    } else {
                        val title = when(state.filter) {
                            LaunchesFilter.PAST -> getString(R.string.fragmentLaunchesRecentFilterScreenTitle)
                            LaunchesFilter.UPCOMING -> getString(R.string.fragmentLaunchesUpcomingFilterScreenTitle)
                            LaunchesFilter.ALL -> getString(R.string.fragmentLaunchesAllFilterScreenTitle)
                        }
                        mainViewModel.setTitle(title)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(
            buildingHandler,
            diffingHandler,
            viewModel
        ) { state: LaunchesState ->
            when (val launches = state.launches) {
                is Resource.Success -> {
                    launches.data.forEach { launch ->
                        log("Launch date for ${launch.missionName} = ${launch.launchDate!!.time}")
                        itemLaunchCard {
                            id(launch.flightNumber)
                            launch(launch)
                            onLaunchClick { model, _, _, _ ->
                                val flightNumber = model.launch().flightNumber
                                LaunchesFragmentDirections.launchDetails(
                                    flightNumber
                                ).let { directions ->
                                    findNavController().navigate(directions)
                                }
                            }
                        }
                    }
                }
                is Resource.Error<List<LaunchMinimal>, *> -> {
                    itemError {
                        id("launch-error")
                        error(getString(R.string.fragmentLaunchesErrorMessage))
                    }
                    launches.data?.forEach { launch ->
                        itemLaunchCard {
                            id(launch.flightNumber)
                            launch(launch)
                            onLaunchClick { model, _, _, _ ->
                                val flightNumber = model.launch().flightNumber
                                LaunchesFragmentDirections.launchDetails(
                                    flightNumber
                                ).let {
                                    findNavController().navigate(it)
                                }
                            }
                        }
                    }
                }
                else -> itemLoading {
                    id("launches-loading")
                    val loadingText = when(state.filter) {
                        LaunchesFilter.PAST -> getString(R.string.fragmentLaunchesLoadingPastMessage)
                        LaunchesFilter.UPCOMING -> getString(R.string.fragmentLaunchesLoadingUpcomingMessage)
                        LaunchesFilter.ALL -> getString(R.string.fragmentLaunchesLoadingAllMessage)
                    }
                    message(loadingText)
                }
            }
        }
    }
}
