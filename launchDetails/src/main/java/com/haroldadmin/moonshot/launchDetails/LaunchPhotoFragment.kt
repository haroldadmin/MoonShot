package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.launchDetails.databinding.FragmentLaunchPhotoBinding
import com.haroldadmin.vector.activityViewModel

class LaunchPhotoFragment: MoonShotFragment() {

    private lateinit var binding: FragmentLaunchPhotoBinding
    private val mainViewModel: MainViewModel by activityViewModel()
    private val args by navArgs<LaunchPhotoFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.hideScaffolding()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLaunchPhotoBinding.inflate(inflater, container, false)
        binding.photo.load(args.photoUrl)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.showScaffolding()
    }

}