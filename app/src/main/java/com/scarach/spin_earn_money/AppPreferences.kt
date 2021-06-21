package com.scarach.spin_earn_money

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.telecom.TelecomManager

class AppPreferences {

    companion object {
        private const val APP_PREFERENCES_FILE_NAME = "app_preferences"
        private var context: Context? = null
        private var sharedPreferencesSingleton: SharedPreferences? = null

        fun initialize(context: Context) {
            AppPreferences.context = context
        }

        // todo @Synchronized
        fun getInstance(): SharedPreferences? {

            if (sharedPreferencesSingleton == null) {
                sharedPreferencesSingleton =
                    context?.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
            }

            return sharedPreferencesSingleton
        }

        fun getString(stringKey: String): String? {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getString(stringKey, null)
        }

        fun getString(stringKey: String, defaultValue: String?): String? {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getString(stringKey, defaultValue)
        }

        fun putString(stringKey: String, value: String): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.edit()?.putString(stringKey, value)?.commit() ?: false
        }

        fun getBoolean(booleanKey: String): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getBoolean(booleanKey, false) ?: false
        }

        fun putBoolean(booleanKey: String, value: Boolean): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.edit()?.putBoolean(booleanKey, value)?.commit() ?: false
        }

        fun getBoolean(booleanKey: String, defaultValue: Boolean): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getBoolean(booleanKey, defaultValue) ?: defaultValue
        }

        fun putInt(intKey: String, value: Int): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.edit()?.putInt(intKey, value)?.commit() ?: false
        }

        fun getInt(intKey: String, defaultValue: Int): Int {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getInt(intKey, defaultValue) ?: defaultValue
        }

        fun putLong(intKey: String, value: Long): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.edit()?.putLong(intKey, value)?.commit() ?: false
        }

        fun getLong(intKey: String, defaultValue: Long): Long {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getLong(intKey, defaultValue) ?: defaultValue
        }

        fun putFloat(intKey: String, value: Float): Boolean {
            val sharedPreferences = getInstance()
            return sharedPreferences?.edit()?.putFloat(intKey, value)?.commit() ?: false
        }

        fun getFloat(intKey: String, defaultValue: Float): Float {
            val sharedPreferences = getInstance()
            return sharedPreferences?.getFloat(intKey, defaultValue) ?: defaultValue
        }

        fun hasKey(key: String): Boolean {
            return getInstance()?.contains(key) ?: false
        }

        fun removeKeyValuePair(key: String) {
            val sharedPreferences = getInstance()
            sharedPreferences?.edit()?.remove(key)?.apply()
        }

        fun getDefaultDialerApp(context: Context): String? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val manager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return manager.defaultDialerPackage
                }
            }
            return null //Change it based on your requirement.
        }
    }

    object PrefKeys {
        const val SPIN_DATE = "SPIN_DATE"
        const val TOTAL_SPIN = "TOTAL_SPIN"
        const val SCRATCH_DATE = "SCRATCH_DATE"
        const val TOTAL_SCRATCH = "TOTAL_SCRATCH"


    }


}