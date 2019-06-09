package com.haroldadmin.moonshot.nextLaunch

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.gone
import com.haroldadmin.moonshot.base.show
import com.haroldadmin.moonshot.core.Resource
import com.haroldadmin.moonshot.databinding.FragmentNextLaunchBinding
import com.haroldadmin.moonshot.models.launch.Launch
import com.haroldadmin.vector.withState
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NextLaunchFragment : MoonShotFragment() {

    private lateinit var binding: FragmentNextLaunchBinding

    private val viewModel by viewModel<NextLaunchViewModel> {
        parametersOf(NextLaunchState())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNextLaunchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, Observer { renderState() })
    }

    override fun renderState() = withState(viewModel) { state ->
        when (state.nextLaunch) {
            is Resource.Success -> binding.apply {
                TransitionManager.beginDelayedTransition(this.root as ViewGroup)
                nextLaunchCard.root.show()
                nextLaunchLoading.root.gone()
                nextLaunchError.root.gone()
                nextLaunchCard.apply {
                    launch = state.nextLaunch.data
                    onLaunchClick = View.OnClickListener {
                        NextLaunchFragmentDirections.launchDetails(state.nextLaunch.data.flightNumber)
                            .let { action -> findNavController().navigate(action) }
                    }
                }
            }

            is Resource.Error<Launch, *> -> binding.apply {
                    TransitionManager.beginDelayedTransition(this.root as ViewGroup)
                    if (state.nextLaunch.data != null) {
                        nextLaunchCard.root.show()
                        nextLaunchCard.launch = state.nextLaunch.data
                    }
                    nextLaunchError.apply {
                        root.show()
                        error = getString(R.string.nextLaunchFragmentErrorMessage)
                    }
                    nextLaunchLoading.root.gone()
                }

            else -> binding.apply {
                    TransitionManager.beginDelayedTransition(this.root as ViewGroup)
                    nextLaunchLoading.root.show()
                    nextLaunchCard.root.gone()
                    nextLaunchError.root.gone()
                }
        }
    }
}