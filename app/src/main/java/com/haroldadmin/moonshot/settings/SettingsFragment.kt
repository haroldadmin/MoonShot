package com.haroldadmin.moonshot.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import com.haroldadmin.moonshot.KEY_CRASH_REPORTS
import com.haroldadmin.moonshot.KEY_THEME_MODE
import com.haroldadmin.moonshot.R
import com.haroldadmin.moonshot.THEME_MAPPINGS
import com.haroldadmin.moonshot.notifications.KEY_LAUNCH_NOTIFICATIONS
import com.haroldadmin.moonshot.notifications.KEY_NOTIFICATION_PADDING
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import com.haroldadmin.moonshot.utils.log
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat() {

    private val launchNotificationsManager by inject<LaunchNotificationManager>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorSurface))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(KEY_LAUNCH_NOTIFICATIONS)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                log("Disabling launch notifications")
                launchNotificationsManager.disableNotifications()
            }
            true
        }

        findPreference<SeekBarPreference>(KEY_NOTIFICATION_PADDING)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue !is Int) return@setOnPreferenceChangeListener false
            log("Setting launch notification padding to $newValue minutes")
            launchNotificationsManager.scheduleNotifications(newValue)
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

        findPreference<SwitchPreferenceCompat>(KEY_CRASH_REPORTS)?.setOnPreferenceChangeListener { _, newValue ->
            view?.let { rootView ->
                Snackbar.make(rootView, R.string.preferencesCrashReportRestartMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Restart") {
                        activity?.recreate()
                    }
                    .show()
            }
            true
        }
    }
}