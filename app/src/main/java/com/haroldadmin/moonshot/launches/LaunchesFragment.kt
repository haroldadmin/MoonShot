package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchesBinding
import com.haroldadmin.moonshot.models.launch.Launch

class LaunchesFragment: MoonShotFragment() {

    private lateinit var binding: FragmentLaunchesBinding
    private val launchesViewModel: LaunchesViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvLaunches.setController(epoxyController())
    }

    override fun invalidate() {
        withState(launchesViewModel) { state ->
            Log.d(this::class.java.simpleName, state.launches.toString())
            binding.rvLaunches.requestModelBuild()
        }
    }

    override fun epoxyController() = simpleController(launchesViewModel) { state ->
        when (val launches = state.launches) {
            is Resource.Success -> {
                launches.data.forEach { launch ->
                    launchItem {
                        id(launch.flightNumber)
                        title(launch.missionName)
                        subtitle(launch.launchYear)
                    }
                }
            }
            is Resource.Error<List<Launch>, *> -> {
                launchItem {
                    id("launch-error")
                    title("Error getting launches")
                }
                launches.data?.forEach { launch ->
                    launchItem {
                        id(launch.flightNumber)
                        title(launch.missionName)
                        subtitle(launch.launchYear)
                    }
                }

            }
            else -> {
                launchItem {
                    id("loading")
                    title("Loading launch item")
                }
            }
        }
    }

}