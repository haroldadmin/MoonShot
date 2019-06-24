package com.haroldadmin.moonshot.settings

import android.content.Context
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
import com.haroldadmin.moonshot.MoonShot
import com.haroldadmin.moonshot.R as appR
import com.haroldadmin.moonshot.THEME_MAPPINGS
import com.haroldadmin.moonshot.notifications.LaunchNotificationManager
import com.haroldadmin.moonshot.sync.SyncManager
import com.haroldadmin.moonshot.utils.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class SettingsFragment : PreferenceFragmentCompat(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job

    private val launchNotificationsManager by inject<LaunchNotificationManager>()
    private val syncManager by inject<SyncManager>()
    private val preferences by lazy {
        requireContext().getSharedPreferences(
            MoonShot.MOONSHOT_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(requireContext(), appR.color.colorSurface))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<SwitchPreferenceCompat>(LaunchNotificationManager.KEY_LAUNCH_NOTIFICATIONS)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == false) {
                log("Disabling launch notifications")
                launch { launchNotificationsManager.disableNotifications() }
            }
            true
        }

        findPreference<SeekBarPreference>(LaunchNotificationManager.KEY_NOTIFICATION_PADDING)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue !is Int) return@setOnPreferenceChangeListener false
            log("Setting launch notification padding to $newValue minutes")
            launch { launchNotificationsManager.scheduleNotifications(newValue) }
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

        findPreference<SwitchPreferenceCompat>(KEY_CRASH_REPORTS)?.setOnPreferenceChangeListener { _, _ ->
            view?.let { rootView ->
                Snackbar.make(rootView, R.string.preferencesCrashReportRestartMessage, Snackbar.LENGTH_SHORT)
                    .setAction("Restart") {
                        activity?.recreate()
                    }
                    .show()
            }
            true
        }

        findPreference<SwitchPreferenceCompat>(SyncManager.KEY_BACKGROUND_SYNC)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue == true) {
                launch { syncManager.enableSync() }
            } else {
                launch { syncManager.disableSync() }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}