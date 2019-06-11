package com.haroldadmin.moonshot.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.haroldadmin.moonshot.KEY_THEME_MODE
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.THEME_MAPPINGS
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_NOTIFICATIONS
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat() {

    private val launchNotificationsManager by inject<LaunchNotificationManager>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(KEY_LAUNCH_NOTIFICATIONS)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                launchNotificationsManager.disableNotifications()
            }
            true
        }

        findPreference<ListPreference>(KEY_THEME_MODE)?.setOnPreferenceChangeListener { _, newValue ->
            val newTheme = THEME_MAPPINGS[newValue]
            if (newTheme != null) {
                AppCompatDelegate.setDefaultNightMode(newTheme)
                true
            } else {
                false
            }
        }
    }
}