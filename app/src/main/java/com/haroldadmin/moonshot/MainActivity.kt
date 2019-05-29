package com.haroldadmin.moonshot

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.haroldadmin.moonshot.base.MoonShotActivity
import com.haroldadmin.moonshot.databinding.ActivityMainBinding

class MainActivity : MoonShotActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)
        binding.mainToolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        binding.mainBottomNav.setupWithNavController(navController)
    }
}
