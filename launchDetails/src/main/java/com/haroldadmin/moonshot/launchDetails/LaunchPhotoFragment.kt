package com.haroldadmin.moonshot.launchDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.airbnb.epoxy.EpoxyRecyclerView
import com.haroldadmin.moonshot.MainViewModel
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.launchDetails.views.fullscreenPhotoView
import com.haroldadmin.vector.activityViewModel

class LaunchPhotoFragment : MoonShotFragment() {

    private val mainViewModel: MainViewModel by activityViewModel()
    private val args by navArgs<LaunchPhotoFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_launch_photo, container, false)
        val carousel: EpoxyRecyclerView = root.findViewById(R.id.carousel)

        carousel.apply {

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

        goFullScreen()

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.showScaffolding()
    }

    private fun goFullScreen() {
        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        mainViewModel.hideScaffolding()
    }

}