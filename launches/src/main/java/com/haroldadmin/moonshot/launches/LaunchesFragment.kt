package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.launches.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshotRepository.launch.LaunchesFilter
import com.haroldadmin.vector.activityViewModel
import com.haroldadmin.moonshot.R as appR

class LaunchesFragment : ComplexMoonShotFragment<LaunchesViewModel, LaunchesState>() {

    private lateinit var binding: FragmentLaunchesBinding

    private val safeArgs by navArgs<LaunchesFragmentArgs>()

    override val viewModel by navGraphViewModels<LaunchesViewModel>(appR.id.launchesFlow) {
        LaunchesViewModelFactory(
            LaunchesState(
                type = safeArgs.type,
                siteId = safeArgs.siteId
            )
        )
    }
    private val mainViewModel: MainViewModel by activityViewModel()

    override fun initDI() = Launches.init()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)

        mainViewModel.setTitle(getString(appR.string.title_launches))

        binding.rvLaunches.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }

        binding.fabFilter.apply {
            setOnClickListener {
                findNavController().navigate(LaunchesFragmentDirections.showFilters())
            }
        }

        return binding.root
    }

    override fun renderer(state: LaunchesState) {
        epoxyController.setData(state)
        if (state.siteName != null) {
            mainViewModel.setTitle(state.siteName)
        } else {
            val title = when (state.filter) {
                LaunchesFilter.PAST -> getString(R.string.fragmentLaunchesRecentFilterScreenTitle)
                LaunchesFilter.UPCOMING -> getString(R.string.fragmentLaunchesUpcomingFilterScreenTitle)
                LaunchesFilter.ALL -> getString(R.string.fragmentLaunchesAllFilterScreenTitle)
            }
            mainViewModel.setTitle(title)
        }
    }

    override val epoxyController by lazy {
        asyncTypedEpoxyController(viewModel) { state: LaunchesState ->
            when (val launches = state.launches) {
                is Resource.Success -> {
                    launches.data.forEach { launch ->
                        launchCard {
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
                    errorView {
                        id("launch-error")
                        errorText(getString(R.string.fragmentLaunchesErrorMessage))
                    }
                    launches.data?.forEach { launch ->
                        launchCard {
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
                else -> loadingView {
                    id("launches-loading")
                    loadingText(
                        when (state.filter) {
                            LaunchesFilter.PAST -> getString(R.string.fragmentLaunchesLoadingPastMessage)
                            LaunchesFilter.UPCOMING -> getString(R.string.fragmentLaunchesLoadingUpcomingMessage)
                            LaunchesFilter.ALL -> getString(R.string.fragmentLaunchesLoadingAllMessage)
                        }
                    )
                }
            }
        }
    }
}
