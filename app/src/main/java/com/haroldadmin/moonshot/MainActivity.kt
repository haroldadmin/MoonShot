package com.haroldadmin.moonshot

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.crashlytics.android.Crashlytics
import com.haroldadmin.moonshot.base.MoonShotActivity
import com.haroldadmin.moonshot.databinding.ActivityMainBinding
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.vector.viewModel
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import io.fabric.sdk.android.Fabric
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val KEY_THEME_MODE = "theme-mode"
const val KEY_CRASH_REPORTS = "crash-reports"

val THEME_MAPPINGS = mapOf(
    "light" to AppCompatDelegate.MODE_NIGHT_NO,
    "dark" to AppCompatDelegate.MODE_NIGHT_YES,
    "auto" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
)

class MainActivity : MoonShotActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        initPreferences(settings)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

        launch {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initUi() {
        setSystemUiFlags()
        initNavController()
        initToolbar()
        initBottomNav()
    }

    private fun initNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setSystemUiFlags() {
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun initToolbar() {
        binding.mainToolbar.apply {
            setupWithNavController(
                navController, AppBarConfiguration(
                    topLevelDestinationIds = setOf(
                        R.id.nextLaunch,
                        R.id.launchesFlow,
                        R.id.rockets,
                        R.id.search
                    )
                )
            )

            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuSearch -> {
                        // Navigating to Search as a menu item navigation destination resets the
                        //  backstack, so we handle it separately here
                        navController.navigate(R.id.search)
                        true
                    }
                    else -> menuItem.onNavDestinationSelected(navController)
                }
            }
            doOnApplyWindowInsets { view, insets, initialState ->
                view.updatePadding(
                    top = initialState.paddings.top + insets.systemWindowInsetTop,
                    left = initialState.paddings.left + insets.systemWindowInsetLeft,
                    right = initialState.paddings.right + insets.systemWindowInsetRight
                )
            }
        }
    }

    private fun initBottomNav() {
        binding.mainBottomNav.apply {
            setupWithNavController(navController)
            doOnApplyWindowInsets { view, insets, initialState ->
                view.updatePadding(
                    bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom,
                    left = initialState.paddings.left + insets.systemWindowInsetLeft,
                    right = initialState.paddings.right + insets.systemWindowInsetRight
                )
            }
        }
    }

    private fun renderState(state: ScaffoldingState) {
        state.toolbarTitle()?.let { title ->
            // If title had already been set, invoking toolbarTitle would return null and this block won't run
            binding.mainToolbar.title = title
        }
        if (state.shouldHideScaffolding) {
            hideScaffolding()
        } else {
            showScaffolding()
        }
    }

    private fun hideScaffolding() {
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE
        binding.motionLayout.apply {
            transitionToState(R.id.noScaffolding)
            getTransition(R.id.collapseScaffolding).setEnable(false)
        }
    }

    private fun showScaffolding() {
        binding.root.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        binding.motionLayout.apply {
            transitionToState(R.id.uncollapsedScaffolding)
            getTransition(R.id.collapseScaffolding).setEnable(true)
        }
    }

    private fun initPreferences(settings: SharedPreferences) {
        initTheme(settings)
        initCrashReporting(settings)
    }

    private fun initTheme(settings: SharedPreferences) {
        val theme = settings.getString(KEY_THEME_MODE, "auto") ?: return
        THEME_MAPPINGS[theme]?.let { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        } ?: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    private fun initCrashReporting(preferences: SharedPreferences) {
        val crashReportingEnabled = preferences.getBoolean(KEY_CRASH_REPORTS, true)
        if (crashReportingEnabled && !BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
        }
    }
}

fun MainActivity.inject() {
    appComponent().inject(this)
}