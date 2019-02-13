package com.example.parsiphal.pregnantbase.data

import android.content.Context

const val PREFS_FILENAME = "com.example.parsiphal.pregnantbase.prefs"
const val MAINT = "maintenance"
const val DISTRICT = "district"

class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var maintenance: Boolean?
        get() = prefs.getBoolean(MAINT, false)
        set(value) = prefs.edit().putBoolean(MAINT, value!!).apply()

    var district: Int
        get() = prefs.getInt(DISTRICT, 1)
        set(value) = prefs.edit().putInt(DISTRICT, value).apply()
}