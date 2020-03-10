package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.launchDetails.databinding.FragmentLaunchPhotoBinding
import com.haroldadmin.moonshot.launchDetails.views.fullscreenPhotoView
import com.haroldadmin.vector.activityViewModel

class LaunchPhotoFragment : MoonShotFragment() {

    private var _binding: FragmentLaunchPhotoBinding? = null
    private val binding: FragmentLaunchPhotoBinding
        get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModel()
    private val args by navArgs<LaunchPhotoFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLaunchPhotoBinding.inflate(inflater, container, false)

        binding.carousel.apply {

            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            withModels {
                args.urls.forEachIndexed { index, url ->
                    fullscreenPhotoView {
                        id("photo-$index")
                        url(url)
                    }
                }
            }

            PagerSnapHelper().attachToRecyclerView(this)
            post { smoothScrollToPosition(args.startIndex) }
        }

        mainViewModel.hideScaffolding()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            mainViewModel.showScaffolding()
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
