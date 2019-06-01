package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunch
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsArgs
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsState
import com.haroldadmin.moonshot.launchItem
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.utils.format

class LaunchesFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchesBinding
    private val launchesViewModel: LaunchesViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        binding.rvLaunches.setController(epoxyController())

        DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).let { divider ->
            binding.rvLaunches.addItemDecoration(divider)
        }

        return binding.root
    }

    override fun invalidate() {
        withState(launchesViewModel) { state ->
            Log.d(this::class.java.simpleName, state.launches.toString())
            binding.rvLaunches.requestModelBuild()
        }
    }

    override fun epoxyController() = simpleController(launchesViewModel) { state ->

        when (val nextLaunch = state.nextLaunch) {
            is Resource.Success -> {
                itemLaunch {
                    id("$nextLaunch.data.flightNumber-next")
                    launch(nextLaunch.data)
                    clickListener { model, _, _, _ ->
                        val flightNumber = model.launch().flightNumber
                        findNavController().navigate(
                            R.id.launchDetails,
                            bundleOf(MvRx.KEY_ARG to LaunchDetailsArgs(flightNumber))
                        )
                    }
                }
            }
            is Resource.Error<Launch, *> -> {
                itemError {
                    id("next-launch-error")
                    error("Unable to fetch next launch")
                }
                nextLaunch.data?.let { cachedLaunch ->
                    itemLaunch {
                        id("$cachedLaunch.flightNumber-next")
                        launch(cachedLaunch)
                        clickListener { model, _, _, _ ->
                            val flightNumber = model.launch().flightNumber
                            findNavController().navigate(
                                R.id.launchDetails,
                                bundleOf(MvRx.KEY_ARG to LaunchDetailsArgs(flightNumber))
                            )
                        }
                    }
                }
            }
        }

        when (val launches = state.launches) {
            is Resource.Success -> {
                launches.data.forEach { launch ->
                    itemLaunch {
                        id(launch.flightNumber)
                        launch(launch)
                        clickListener { model, _, _, _ ->
                            val flightNumber = model.launch().flightNumber
                            findNavController().navigate(
                                R.id.launchDetails,
                                bundleOf(MvRx.KEY_ARG to LaunchDetailsArgs(flightNumber))
                            )
                        }
                    }
                }
            }
            is Resource.Error<List<Launch>, *> -> {
                itemError {
                    id("launch-error")
                    error("Error getting launches")
                }
                launches.data?.forEach { launch ->
                    itemLaunch {
                        id(launch.flightNumber)
                        launch(launch)
                        clickListener { model, _, _, _ ->
                            val flightNumber = model.launch().flightNumber
                            findNavController().navigate(
                                R.id.launchDetails,
                                bundleOf(MvRx.KEY_ARG to LaunchDetailsArgs(flightNumber))
                            )
                        }
                    }
                }
            }
            else -> itemLoading {
                id("launches-loading")
                message("Loading All Launches")
            }
        }
    }
}