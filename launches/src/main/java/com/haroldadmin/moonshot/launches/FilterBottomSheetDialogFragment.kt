package com.haroldadmin.moonshot.launches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.logger.logD
import com.haroldadmin.moonshot.launches.databinding.FragmentFilterBottomSheetDialogBinding
import com.haroldadmin.moonshot.launches.views.filterOptionView
import com.haroldadmin.moonshotRepository.launch.LaunchType
import kotlinx.coroutines.ExperimentalCoroutinesApi

const val LaunchFilterKey = "com.haroldadmin.moonshot.launches.LaunchFilterKey"

@ExperimentalCoroutinesApi
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentFilterBottomSheetDialogBinding? = null
    private val binding: FragmentFilterBottomSheetDialogBinding
        get() = _binding!!

    private val onFilterClick: (LaunchType) -> Unit = { filter ->
        val launchesBackstackEntry = findNavController().previousBackStackEntry ?: error(
            "Can not find LaunchesFragment on the backstack"
        )

        logD { "Previous entry: ${launchesBackstackEntry.destination}" }

        launchesBackstackEntry
            .savedStateHandle
            .set(LaunchFilterKey, filter.name)
            .also {
                dismiss()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBottomSheetDialogBinding.inflate(inflater, container, false)
        binding.rvFilter.apply {
            setControllerAndBuildModels(epoxyController)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val epoxyController = simpleController {
        filterOptionView {
            id("all")
            name(getString(R.string.fragmentFilterSheetAllFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchType.All) }
        }
        filterOptionView {
            id("past")
            name(getString(R.string.fragmentFilterSheetPastFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchType.Recent) }
        }
        filterOptionView {
            id("upcoming")
            name(getString(R.string.fragmentFilterSheetUpcomingFilterName))
            onFilterSelected { _ -> onFilterClick(LaunchType.Upcoming) }
        }
    }
}

@ExperimentalCoroutinesApi
private fun FilterBottomSheetDialogFragment.simpleController(
    buildModelsCallback: EpoxyController.() -> Unit
): EpoxyController = object : EpoxyController() {
    override fun buildModels() {
        buildModelsCallback()
    }
}
