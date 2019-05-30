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
import com.haroldadmin.moonshot.launchItem
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.moonshot.utils.format

class LaunchesFragment : MoonShotFragment() {

    private lateinit var binding: FragmentLaunchesBinding
    private val launchesViewModel: LaunchesViewModel by fragmentViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchesBinding.inflate(inflater, container, false)
        binding.rvLaunches.setController(epoxyController())
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
                launchItem {
                    id("next launch", nextLaunch.data.missionName)
                    title(nextLaunch.data.missionName)
                    subtitle("""
                        |Description:
                        |${nextLaunch.data.details}
                        |
                        |Launch Date:
                        |${nextLaunch.data.launchDate.format(resources.configuration)}""".trimMargin())
                }
            }
            is Resource.Error<Launch, *> -> {
                launchItem {
                    id("next-launch-error")
                    title("Error fetching next launch")
                    subtitle(nextLaunch.error.toString())
                }
                nextLaunch.data?.let { cachedLaunch ->
                    launchItem {
                        id("cached-next-launch")
                        title(cachedLaunch.missionName)
                        subtitle("""
                        |Description:
                        |${cachedLaunch.details}
                        |
                        |Launch Date:
                        |${cachedLaunch.launchDate.format(resources.configuration)}""".trimMargin())
                    }
                }
            }
            else -> launchItem {
                id("next-launch-loading")
                title("Loading next launch")
                subtitle("Hang on")
            }
        }

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