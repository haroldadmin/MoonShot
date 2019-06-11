package com.haroldadmin.moonshot

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.haroldadmin.moonshot.base.MoonShotActivity
import com.haroldadmin.moonshot.databinding.ActivityMainBinding

const val KEY_THEME_MODE = "theme-mode"

val THEME_MAPPINGS = mapOf(
    "light" to AppCompatDelegate.MODE_NIGHT_NO,
    "dark" to AppCompatDelegate.MODE_NIGHT_YES,
    "auto" to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
)

class MainActivity : MoonShotActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUiTheme()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = findNavController(R.id.navHostFragment)

        with(binding) {

            root.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

            mainBottomNav.setupWithNavController(navController)
            mainToolbar.apply {
                setupWithNavController(navController, AppBarConfiguration(navController.graph))
                inflateMenu(R.menu.menu_main)
                setOnMenuItemClickListener { menuItem ->
                    menuItem.onNavDestinationSelected(navController)
                }
            }
        }
    }

    private fun initUiTheme() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val theme = preferences.getString(KEY_THEME_MODE, "auto")
        THEME_MAPPINGS[theme]?.let { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        } ?: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
