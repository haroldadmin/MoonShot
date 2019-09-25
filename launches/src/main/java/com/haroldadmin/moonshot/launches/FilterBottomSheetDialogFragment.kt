package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.launches.databinding.FragmentFilterBottomSheetDialogBinding
import com.haroldadmin.moonshot.launches.views.filterOptionView
import com.haroldadmin.moonshotRepository.launch.LaunchesFilter

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

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
        filterOptionView {
            id("all")
            name(getString(R.string.fragmentFilterSheetAllFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchesFilter.ALL) }
        }
        filterOptionView {
            id("past")
            name(getString(R.string.fragmentFilterSheetPastFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchesFilter.PAST) }
        }
        filterOptionView {
            id("upcoming")
            name(getString(R.string.fragmentFilterSheetUpcomingFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchesFilter.UPCOMING) }
        }
    }
}

private fun FilterBottomSheetDialogFragment.simpleController(
    buildModelsCallback: EpoxyController.() -> Unit
): EpoxyController = object : EpoxyController() {
    override fun buildModels() {
        buildModelsCallback()
    }
}
