package com.haroldadmin.moonshot.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.haroldadmin.moonshot.R
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
    }
}