package com.haroldadmin.moonshot

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.crashlytics.android.Crashlytics
import com.haroldadmin.moonshot.base.MoonShotActivity
import com.haroldadmin.moonshot.databinding.ActivityMainBinding
import com.haroldadmin.moonshot.di.appComponent
import com.haroldadmin.vector.viewModel
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

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()

    @Inject
    @Named("settings")
    lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)

        initPreferences(settings)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)

        with(binding) {

            root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

            mainBottomNav.setupWithNavController(navController)
            mainToolbar.apply {
                val appBarConfig = AppBarConfiguration(setOf(R.id.nextLaunch, R.id.launchesFlow, R.id.rockets, R.id.search))
                setupWithNavController(navController, appBarConfig)
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
            }
        }
        launch {
            viewModel.state.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: ScaffoldingState) {
        state.toolbarTitle()?.let { title ->
            // If title had already been set, invoking toolbarTitle would return null and this block won't run
            binding.mainToolbar.title = title
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