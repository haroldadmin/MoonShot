package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.launches.databinding.FragmentFilterBottomSheetDialogBinding

class FilterBottomSheetDialogFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBottomSheetDialogBinding
    private val viewModel by navGraphViewModels<LaunchesViewModel>(appR.id.launchesFlow)
    private val onFilterClick: (LaunchesFilter) -> Unit = { filter ->
        viewModel.setFilter(filter)
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBottomSheetDialogBinding.inflate(inflater, container, false)
        binding.rvFilter.apply {
            setControllerAndBuildModels(epoxyController)
        }
        return binding.root
    }

    private val epoxyController = simpleController {
        itemFilterOption {
            id("all")
            filterOption(LaunchesFilter.ALL)
            onFilterSelected { model, _, _, _ -> onFilterClick(model.filterOption()) }
        }
        itemFilterOption {
            id("past")
            filterOption(LaunchesFilter.PAST)
            onFilterSelected { model, _, _, _ -> onFilterClick(model.filterOption()) }
        }
        itemFilterOption {
            id("upcoming")
            filterOption(LaunchesFilter.UPCOMING)
            onFilterSelected { model, _, _, _ -> onFilterClick(model.filterOption()) }
        }
    }
}

private fun Fragment.simpleController(
    buildModelsCallback: EpoxyController.() -> Unit
): EpoxyController = object: EpoxyController() {
    override fun buildModels() {
        buildModelsCallback()
    }
}
