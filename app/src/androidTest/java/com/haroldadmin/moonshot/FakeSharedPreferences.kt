package com.haroldadmin.moonshot

import android.content.SharedPreferences
import javax.inject.Inject

class FakeSharedPreferences @Inject constructor() : SharedPreferences {

    private val prefs = mutableMapOf<String, Any?>()

    override fun contains(key: String?): Boolean {
        return key in prefs
    }

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return prefs[key] as? Boolean? ?: defValue
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) {
        throw UnsupportedOperationException("Fake SharedPreferences does not support listeners")
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return prefs[key] as? Int ?: defValue
    }

    override fun getAll(): MutableMap<String, *> {
        return prefs
    }

    override fun edit(): SharedPreferences.Editor {
        return FakeEditor(this)
    }

    override fun getLong(key: String?, defValue: Long): Long {
        return prefs[key] as? Long ?: defValue
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return prefs[key] as? Float ?: defValue
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        @Suppress("UNCHECKED_CAST")
        return prefs[key] as? MutableSet<String> ?: defValues ?: mutableSetOf()
    }

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) {
        throw UnsupportedOperationException("Fake SharedPreferences does not support listeners")
    }

    override fun getString(key: String?, defValue: String?): String? {
        return prefs[key] as? String ?: defValue
    }

    class FakeEditor (preferences: FakeSharedPreferences): SharedPreferences.Editor {

        private val prefs = preferences.prefs

        override fun clear(): SharedPreferences.Editor {
            prefs.clear()
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            putValue(key, value)
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            putValue(key, value)
            return this
        }

        override fun remove(key: String): SharedPreferences.Editor {
            prefs.remove(key)
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            putValue(key, value)
            return this
        }

        override fun putStringSet(key: String, values: MutableSet<String>?): SharedPreferences.Editor {
            putValue(key, values)
            return this
        }

        override fun commit(): Boolean {
            return true
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            putValue(key, value)
            return this
        }

        override fun apply() {
            // no-op
        }

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            putValue(key, value)
            return this
        }

        private fun putValue(key: String, value: Any?) {
            prefs[key] = value
        }

    }
}