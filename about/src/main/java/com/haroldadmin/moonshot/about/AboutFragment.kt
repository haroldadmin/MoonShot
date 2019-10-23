package com.haroldadmin.moonshot.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haroldadmin.moonshot.base.layoutAnimation
import com.haroldadmin.moonshot.about.databinding.FragmentAboutBinding
import com.haroldadmin.moonshot.about.views.aboutAppCard
import com.haroldadmin.moonshot.about.views.aboutDetailCard
import com.haroldadmin.moonshot.base.MoonShotFragment
import com.haroldadmin.moonshot.base.simpleController
import com.haroldadmin.moonshot.R as appR

class AboutFragment : MoonShotFragment() {

    private lateinit var binding: FragmentAboutBinding
    private val initialState = AboutState()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.rvAbout.apply {
            setController(epoxyController)
            layoutAnimation = layoutAnimation(appR.anim.layout_animation_fade_in)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        epoxyController.requestModelBuild()
    }

    private val epoxyController by lazy {
        simpleController {
            aboutAppCard {
                id("about")
                state(initialState)
            }

            aboutDetailCard {
                id("play-store")
                header(getString(R.string.aboutFragmentRateHeader))
                message(getString(R.string.aboutFragmentRateMessage))
                icon(R.drawable.ic_round_star_rate_18px)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${requireContext().packageName}"))
                        .also { intent ->
                            startActivity(intent)
                        }
                }
            }

            aboutDetailCard {
                id("author")
                header(getString(R.string.aboutFragmentAuthorHeader))
                message(getString(R.string.aboutFragmentAuthorName))
                icon(R.drawable.ic_github_round)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(Links.AUTHOR))
                        .also { intent ->
                            startActivity(intent)
                        }
                }
            }

            aboutDetailCard {
                id("repo")
                header(getString(R.string.aboutFragmentRepositoryHeader))
                message(getString(appR.string.app_name))
                icon(R.drawable.ic_github_round)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(Links.APP_REPO)).also { intent ->
                        startActivity(intent)
                    }
                }
            }

            aboutDetailCard {
                id("vector")
                header(getString(R.string.aboutFragmentArchitectureHeader))
                message(getString(R.string.aboutFragmentArchitectureMessage))
                icon(R.drawable.ic_round_details_24px)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(Links.VECTOR)).also { intent ->
                        startActivity(intent)
                    }
                }
            }

            aboutDetailCard {
                id("spacex-api-credits")
                header(getString(R.string.aboutFragmentApiCreditsHeader))
                message(getString(R.string.aboutFragmentApiCreditsMessage))
                icon(R.drawable.ic_github_round)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(Links.SPACEX_API)).also { intent ->
                        startActivity(intent)
                    }
                }
            }

            aboutDetailCard {
                id("privacy-policy")
                header(getString(R.string.aboutFragmentPrivacyPolicyHeader))
                message(getString(R.string.aboutFragmentPrivacyPolicyMessage))
                icon(R.drawable.ic_round_insert_drive_file_24px)
                onDetailClick { _ ->
                    Intent(Intent.ACTION_VIEW, Uri.parse(Links.PRIVACY_POLICY)).also { intent ->
                        startActivity(intent)
                    }
                }
            }

            if (BuildConfig.DEBUG) {
                aboutDetailCard {
                    id("debug-build")
                    header("Debug build")
                    message("You're running a debug build of the app")
                    icon(appR.drawable.ic_launcher_foreground)
                }
            }
        }
    }
}