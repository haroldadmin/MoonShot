package com.haroldadmin.moonshot

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.crashlytics.android.Crashlytics
import com.haroldadmin.moonshot.base.MoonShotActivity
import com.haroldadmin.moonshot.core.Consumable
import com.haroldadmin.moonshot.databinding.ActivityMainBinding
import com.haroldadmin.vector.withState
import io.fabric.sdk.android.Fabric
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val KEY_THEME_MODE = "theme-mode"
const val KEY_CRASH_REPORTS = "crash-reports"

val THEME_MAPPINGS = mapOf(
    "light" to AppCompatDelegate.MODE_NIGHT_NO,
    "dark" to AppCompatDelegate.MODE_NIGHT_YES,
    "auto" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
)

private const val KEY_TOOLBAR_TITLE = "toolbar-title"

class MainActivity : MoonShotActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModel<MainViewModel> {
        parametersOf(
            ScaffoldingState(toolbarTitle = Consumable(getString(R.string.app_name)))
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.getDefaultSharedPreferences(this).also {
            initPreferences(it)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)

        with(binding) {

            root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

            mainBottomNav.setupWithNavController(navController)
            mainToolbar.apply {
                val appBarConfig = AppBarConfiguration(setOf(R.id.nextLaunch, R.id.launches, R.id.rockets))
                setupWithNavController(navController, appBarConfig)
                inflateMenu(R.menu.menu_main)
                setOnMenuItemClickListener { menuItem ->
                    menuItem.onNavDestinationSelected(navController)
                }
                savedInstanceState?.let { bundle ->
                    title = bundle.getCharSequence(KEY_TOOLBAR_TITLE)
                }
            }
        }
        viewModel.state.observe(this, Observer { renderState() })
    }

    private fun renderState() = withState(viewModel) { state ->
        state.toolbarTitle()?.let { title ->
            // If title had already been set, invoking toolbarTitle would return null and this block won't run
            binding.mainToolbar.title = title
        }
    }

    private fun initPreferences(preferences: SharedPreferences) {
        initTheme(preferences)
        initCrashReporting(preferences)
    }

    private fun initTheme(preferences: SharedPreferences) {
        val theme = preferences.getString(KEY_THEME_MODE, "auto") ?: return
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_TOOLBAR_TITLE, binding.mainToolbar.title)
    }
}
