package com.haroldadmin.moonshot.rockets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.typedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentRocketsBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.itemRocket
import com.haroldadmin.moonshot.models.rocket.Rocket
import com.haroldadmin.vector.withState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RocketsFragment : MoonShotFragment() {

    private lateinit var binding: FragmentRocketsBinding
    private val viewModel by viewModel<RocketsViewModel> {
        parametersOf(RocketsState())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRocketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRockets.setController(epoxyController)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        epoxyController.cancelPendingModelBuild()
    }

    private val epoxyController by lazy {
        typedEpoxyController(viewModel) { state ->
            when (val rockets = state.rockets) {
                is Resource.Success -> {
                    rockets.data.forEach { rocket ->
                        itemRocket {
                            id(rocket.rocketId)
                            rocket(rocket)
                        }
                    }
                }
                is Resource.Error<List<Rocket>, *> -> {
                    itemError {
                        id("error-rockets")
                        error("Error loading rockets")
                    }
                    rockets.data?.forEach { rocket ->
                        itemRocket {
                            id(rocket.rocketId)
                            rocket(rocket)
                        }
                    }
                }
                else -> itemLoading {
                    id("loading-rockets")
                    message("Loading Rockets")
                }
            }
        }
    }

    override fun renderState() = withState(viewModel) { state ->
        epoxyController.setData(state)
    }
}