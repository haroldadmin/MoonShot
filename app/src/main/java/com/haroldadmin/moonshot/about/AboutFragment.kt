package com.haroldadmin.moonshot.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.databinding.FragmentAboutBinding
import com.haroldadmin.moonshot.itemAboutApp
import com.haroldadmin.moonshot.itemAboutDetail

class AboutFragment : MoonShotFragment() {

    private lateinit var binding: FragmentAboutBinding
    private val initialState = AboutState()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.rvAbout.apply {
            setController(epoxyController)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fade_in)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderState()
    }

    override fun renderState() {
        epoxyController.requestModelBuild()
    }

    private val epoxyController by lazy {
        simpleController {
            itemAboutApp {
                id("about")
                state(initialState)
            }

            itemAboutDetail {
                id("play-store")
                header(getString(R.string.aboutFragmentRateHeader))
                message(getString(R.string.aboutFragmentRateMessage))
                icon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_star_rate_18px))
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}"))
                        .also { intent ->
                            startActivity(intent)
                        }
                }
            }

            itemAboutDetail {
                id("author")
                header(getString(R.string.aboutFragmentAuthorHeader))
                message(getString(R.string.aboutFragmentAuthorName))
                icon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_github_round))
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://haroldadmin.github.io"))
                        .also { intent ->
                            startActivity(intent)
                        }
                }
            }

            itemAboutDetail {
                id("repo")
                header(getString(R.string.aboutFragmentRepositoryHeader))
                message(getString(R.string.app_name))
                icon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_github_round))
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/haroldadmin/MoonShot")).also { intent ->
                        startActivity(intent)
                    }
                }
            }

            itemAboutDetail {
                id("vector")
                header(getString(R.string.aboutFragmentArchitectureHeader))
                message(getString(R.string.aboutFragmentArchitectureMessage))
                icon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_details_24px))
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/haroldadmin/Vector")).also { intent ->
                        startActivity(intent)
                    }
                }
            }
        }
    }
}