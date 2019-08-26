package com.haroldadmin.moonshot.search

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.base.MoonShotAsyncTypedEpoxyController
import com.haroldadmin.moonshot.base.MoonShotState
import com.haroldadmin.moonshot.base.MoonShotViewModel
import com.haroldadmin.moonshot.core.invoke
import com.haroldadmin.moonshot.launchDetails.LaunchDetailsFragmentArgs
import com.haroldadmin.moonshot.launchPad.LaunchPadFragmentArgs
import com.haroldadmin.moonshot.rocketDetails.RocketDetailsFragmentArgs
import com.haroldadmin.moonshot.search.databinding.FragmentSearchBinding
import com.haroldadmin.vector.withState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import kotlin.coroutines.CoroutineContext

@FlowPreview
@ExperimentalCoroutinesApi
class SearchFragment : BottomSheetDialogFragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.Main + Job()

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel> {
        parametersOf(SearchState())
    }
    private val differ by inject<Handler>(named("differ"))
    private val builder by inject<Handler>(named("builder"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Search.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        launch {
            binding
                .etQuery
                .textChanges()
                .debounce(500)
                .collect { viewModel.searchFor(it, 10) }
        }

        binding.rvSearchResults.apply {
            setController(controller)
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        launch {
            viewModel.state.collect { state ->
                controller.setData(state)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.container.updateLayoutParams<FrameLayout.LayoutParams> {
            height = screenHeight
        }
    }

    private val controller by lazy {
        asyncTypedEpoxyController(builder, differ, viewModel) { state ->
            if (state.isUninitialized) {
                itemSearchUninitialized {
                    id("search-uninitialized")
                }

            } else {

                state.launches()?.forEach { launch ->
                    itemSearchResult {
                        id(launch.flightNumber)
                        resultType(R.string.searchResultTypeLaunch)
                        result(launch.missionName ?: "Unknown")
                        onClick { _ ->
                            val args = LaunchDetailsFragmentArgs(launch.flightNumber)
                            findNavController().navigate(appR.id.launchDetails, args.toBundle())
                        }
                    }

                }

                state.rockets()?.forEach { rocket ->
                    itemSearchResult {
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
                    itemSearchResult {
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
private fun EditText.textChanges(): Flow<String> {
    val channel = ConflatedBroadcastChannel<CharSequence>()
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) = Unit

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            channel.offer(text)
        }
    })
    return channel
        .asFlow()
        .filterNot { it.isBlank() }
        .map { seq -> seq.toString() }
        .onCompletion { channel.close() }
}

private fun <S : MoonShotState> Fragment.asyncTypedEpoxyController(
    modelBuildingHandler: Handler,
    diffingHandler: Handler,
    viewModel: MoonShotViewModel<S>,
    buildModels: EpoxyController.(state: S) -> Unit
) = MoonShotAsyncTypedEpoxyController<S>(modelBuildingHandler, diffingHandler) {
    if (view == null || isRemoving) return@MoonShotAsyncTypedEpoxyController
    withState(viewModel) { state ->
        buildModels(state)
    }
}

private val BottomSheetDialogFragment.screenHeight: Int
    get() = resources.displayMetrics.heightPixels
