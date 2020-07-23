package com.haroldadmin.moonshot.features.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.moonshot.base.MoonShotAsyncTypedEpoxyController
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.moonshot.features.launchDetails.LaunchDetailsFragmentArgs
import com.haroldadmin.moonshot.features.launchPad.LaunchPadFragmentArgs
import com.haroldadmin.moonshot.features.rocketDetails.RocketDetailsFragmentArgs
import com.haroldadmin.moonshot.features.search.R
import com.haroldadmin.moonshot.features.search.databinding.FragmentSearchBinding
import com.haroldadmin.moonshot.features.search.di.DaggerSearchComponent
import com.haroldadmin.moonshot.features.search.views.searchResultView
import com.haroldadmin.moonshot.features.search.views.searchUninitializedView
import com.haroldadmin.vector.fragmentViewModel
import com.haroldadmin.vector.withState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.haroldadmin.moonshot.R as appR

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding!!

    @Inject lateinit var viewModelFactory: SearchViewModel.Factory

    private val viewModel: SearchViewModel by fragmentViewModel { initState, _ ->
        viewModelFactory.create(initState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSearchComponent.builder()
            .appComponent(appComponent())
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.rvSearchResults.apply {
            setController(controller)
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                controller.setData(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.etQuery
                .textChanges()
                .debounce(500)
                .collect { viewModel.searchFor(it, 10) }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.container.updateLayoutParams<FrameLayout.LayoutParams> {
            height = screenHeight
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        controller.cancelPendingModelBuild()
        _binding = null
    }

    private val controller by lazy {
        asyncTypedEpoxyController(viewModel) { state ->
            if (state.isUninitialized || state.isLoading) {
                searchUninitializedView {
                    id("search-uninitialized")
                }
            } else {
                state.launches()?.forEach { launch ->
                    searchResultView {
                        id(launch.flightNumber)
                        resultType(R.string.searchResultTypeLaunch)
                        result(launch.missionName)
                        onClick { _ ->
                            val args = LaunchDetailsFragmentArgs(launch.flightNumber)
                            findNavController().navigate(appR.id.launchDetails, args.toBundle())
                        }
                    }
                }

                state.rockets()?.forEach { rocket ->
                    searchResultView {
                        id(rocket.rocketId)
                        resultType(R.string.searchResultTypeRocket)
                        result(rocket.rocketName)
                        onClick { _ ->
                            val args = RocketDetailsFragmentArgs(rocket.rocketId)
                            findNavController().navigate(appR.id.rocketDetails, args.toBundle())
                        }
                    }
                }

                state.launchPads()?.forEach { launchPad ->
                    searchResultView {
                        id(launchPad.id)
                        resultType(R.string.searchResultTypeLaunchPad)
                        result(launchPad.siteNameLong)
                        onClick { _ ->
                            val args = LaunchPadFragmentArgs(launchPad.siteId)
                            findNavController().navigate(appR.id.launchPad, args.toBundle())
                        }
                    }
                }
            }
        }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
private fun EditText.textChanges(): Flow<String> = callbackFlow<CharSequence> {
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            channel.offer(text)
        }
    }

    this@textChanges.addTextChangedListener(textWatcher)
    awaitClose { this@textChanges.removeTextChangedListener(textWatcher) }
}.map { seq -> seq.toString() }

private fun <S : MoonShotState> Fragment.asyncTypedEpoxyController(
    viewModel: MoonShotViewModel<S>,
    buildModels: EpoxyController.(state: S) -> Unit
) = MoonShotAsyncTypedEpoxyController<S>() {
    if (view == null || isRemoving) return@MoonShotAsyncTypedEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

private val BottomSheetDialogFragment.screenHeight: Int
    get() = resources.displayMetrics.heightPixels
