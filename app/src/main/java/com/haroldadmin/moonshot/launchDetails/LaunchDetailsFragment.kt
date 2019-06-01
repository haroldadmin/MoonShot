package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.fragmentViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentLaunchDetailsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunch
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.models.launch.Launch

class LaunchDetailsFragment: MoonShotFragment() {

    private lateinit var binding: FragmentLaunchDetailsBinding
    private val viewModel: LaunchDetailsViewModel by fragmentViewModel()

    override fun epoxyController() = simpleController(viewModel) { state ->
        when (state.launch) {
            is Resource.Success -> itemLaunch {
                id(state.launch.data.flightNumber)
                launch(state.launch.data)
            }
            is Resource.Error<Launch, *> -> itemError {
                id("launch-error")
                error(state.launch.error.toString())
            }
            else -> itemLoading {
                id("launch-loading")
                message("Loding launch details")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchDetailsBinding.inflate(inflater, container, false)
        binding.rvLaunchDetails.setController(epoxyController())
        return binding.root
    }

    override fun invalidate() {
        binding.rvLaunchDetails.requestModelBuild()
    }

}