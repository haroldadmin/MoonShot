package com.haroldadmin.moonshot.nextLaunch

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyController
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.asyncTypedEpoxyController
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentNextLaunchBinding
import com.haroldadmin.moonshot.itemError
import com.haroldadmin.moonshot.itemLaunchCard
import com.haroldadmin.moonshot.itemLaunchDetail
import com.haroldadmin.moonshot.itemLoading
import com.haroldadmin.moonshot.models.launch.LaunchMinimal
import com.haroldadmin.moonshot.utils.format
import com.haroldadmin.vector.withState
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class NextLaunchFragment : MoonShotFragment() {

    private lateinit var binding: FragmentNextLaunchBinding
    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))
    private val viewModel by viewModel<NextLaunchViewModel> {
        parametersOf(NextLaunchState())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNextLaunchBinding.inflate(inflater, container, false)
        binding.rvNextLaunch.setController(epoxyController)
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
        fragmentScope.launch {
            if (state.nextLaunch is Resource.Success) {
                viewModel.persistNextLaunchValues(requireContext())
            }
        }
    }

    private val epoxyController by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            when (val launch = state.nextLaunch) {

                is Resource.Success -> buildLaunchModels(this, launch.data)

                is Resource.Error<LaunchMinimal, *> -> {
                    itemError {
                        id("next-launch-error")
                        error(getString(R.string.nextLaunchFragmentErrorMessage))
                    }
                    if (launch.data != null) {
                        buildLaunchModels(this, launch.data!!)
                    }
                }
                else -> itemLoading {
                    id("next-launch-loading")
                    message(getString(R.string.nextLaunchFragmentLoadingMessage))
                }
            }
        }
    }

    private fun showLaunchDetails(flightNumber: Int) {
        NextLaunchFragmentDirections.launchDetails(flightNumber).let { action ->
            findNavController().navigate(action)
        }
    }

    private fun buildLaunchModels(
        controller: EpoxyController,
        launch: LaunchMinimal
    ) {
        with(controller) {
            itemLaunchCard {
                id(launch.flightNumber)
                launch(launch)
                header(getString(R.string.nextLaunchFragmentNextLaunchHeaderText))
                onLaunchClick { _ -> showLaunchDetails(launch.flightNumber) }
            }

            itemLaunchDetail {
                id("launch-date")
                detailHeader(getString(R.string.launchDetailLaunchDateHeader))
                detailName(
                    launch.launchDate?.format(resources.configuration)
                        ?: getString(R.string.nextLaunchFragmentNoLaunchDateText)
                )
                detailIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_date_range_24px))
            }

            itemLaunchDetail {
                id("launch-site")
                detailHeader(getString(R.string.launchDetailLaunchSiteHeader))
                detailName(launch.siteNameLong)
                detailIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_place_24px))
                onDetailClick { _ ->
                    launch.siteId?.let { id ->
                        val action = NextLaunchFragmentDirections.launchPadDetails(id)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}