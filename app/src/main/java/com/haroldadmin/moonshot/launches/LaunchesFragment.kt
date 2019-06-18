package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.vector.withState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class LaunchesFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchesBinding
    private val safeArgs by navArgs<LaunchesFragmentArgs>()
    private val viewModel by viewModel<LaunchesViewModel> {
        parametersOf(LaunchesState(
            type = safeArgs.type,
            siteId = safeArgs.siteId
        ))
    }
    private val mainViewModel by sharedViewModel<MainViewModel>()
    private val diffingHandler by inject<Handler>(named("differ"))
    private val buildingHandler by inject<Handler>(named("builder"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        val animation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_in)
        binding.rvLaunches.apply {
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

    override fun renderState() = withState(viewModel) { state ->
        epoxyController.setData(state)
        if (state.siteName != null) {
            mainViewModel.setTitle(state.siteName)
        }
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(buildingHandler, diffingHandler, viewModel) { state: LaunchesState ->
            when (val launches = state.launches) {
                is Resource.Success -> {
                    launches.data.forEach { launch ->
                        itemLaunchCard {
                            id(launch.flightNumber)
                            launch(launch)
                            onLaunchClick { model, _, _, _ ->
                                val flightNumber = model.launch().flightNumber
                                LaunchesFragmentDirections.launchDetails(flightNumber).let { directions ->
                                    findNavController().navigate(directions)
                                }
                            }
                        }
                    }
                }
                is Resource.Error<List<LaunchMinimal>, *> -> {
                    itemError {
                        id("launch-error")
                        error(getString(R.string.launchesFragmentErrorMessage))
                    }
                    launches.data?.forEach { launch ->
                        itemLaunchCard {
                            id(launch.flightNumber)
                            launch(launch)
                            onLaunchClick { model, _, _, _ ->
                                val flightNumber = model.launch().flightNumber
                                LaunchesFragmentDirections.launchDetails(flightNumber).let {
                                    findNavController().navigate(it)
                                }
                            }
                        }
                    }
                }
                else -> itemLoading {
                    id("launches-loading")
                    message(getString(R.string.launchesFragmentLoadingMessage))
                }
            }
        }
    }
}
