package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.ComplexMoonShotFragment
import com.haroldadmin.moonshot.base.asyncController
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.launches.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.launches.di.DaggerLaunchesComponent
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.views.errorView
import com.haroldadmin.moonshot.views.launchCard
import com.haroldadmin.moonshot.views.loadMoreView
import com.haroldadmin.moonshot.views.loadingView
import com.haroldadmin.moonshotRepository.launch.LaunchType
import com.haroldadmin.vector.activityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@ExperimentalCoroutinesApi
class LaunchesFragment : ComplexMoonShotFragment<LaunchesViewModel, LaunchesState>() {

    private lateinit var binding: FragmentLaunchesBinding
    @Inject lateinit var viewModelFactory: LaunchesViewModel.Factory
    @Inject lateinit var mainViewModelFactory: MainViewModel.Factory

    private val safeArgs by navArgs<LaunchesFragmentArgs>()

    override val viewModel by navGraphViewModels<LaunchesViewModel>(appR.id.launchesFlow) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return viewModelFactory.create(LaunchesState(type = safeArgs.type, siteId = safeArgs.siteId)) as T
            }
        }
    }

    private val mainViewModel: MainViewModel by activityViewModel { initState, savedStateHandle ->
        mainViewModelFactory.create(initState, savedStateHandle)
    }

    override fun initDI() {
        DaggerLaunchesComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
    }

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
                LaunchType.Recent -> getString(R.string.fragmentLaunchesRecentFilterScreenTitle)
                LaunchType.Upcoming -> getString(R.string.fragmentLaunchesUpcomingFilterScreenTitle)
                LaunchType.All -> getString(R.string.fragmentLaunchesAllFilterScreenTitle)
            }
            mainViewModel.setTitle(title)
        }
    }

    override fun epoxyController() = asyncController(viewModel) { state: LaunchesState ->
        when (state.launchesRes) {
            is Resource.Success -> buildLaunches(state)

            is Resource.Error<List<Launch>, *> -> {
                errorView {
                    id("launch-error")
                    errorText(getString(R.string.fragmentLaunchesErrorMessage))
                }
                buildLaunches(state)
           }
            else -> loadingView {
                id("launches-loading")
                loadingText(
                    when (state.filter) {
                        LaunchType.Recent -> getString(R.string.fragmentLaunchesLoadingPastMessage)
                        LaunchType.Upcoming -> getString(R.string.fragmentLaunchesLoadingUpcomingMessage)
                        LaunchType.All -> getString(R.string.fragmentLaunchesLoadingAllMessage)
                    }
                )
            }
        }
    }

    private fun EpoxyController.buildLaunches(state: LaunchesState) {
        state.launches.forEach { launch ->
            launchCard {
                id(launch.flightNumber)
                launch(launch)
                onLaunchClick { _ ->
                    val action = LaunchesFragmentDirections.launchDetails(launch.flightNumber)
                    findNavController().navigate(action)
                }
            }
        }

        if (state.hasMoreToFetch) {
            loadMoreView {
                id("load-more-launches")
                loadingText("Loading more")
                onBind { _, _, _ -> viewModel.loadMore() }
            }
        }
    }
}
