package com.example.parsiphal.pregnantbase.data

import android.content.Context

const val PREFS_FILENAME = "com.example.parsiphal.pregnantbase.prefs"
const val MAINT = "maintanance"

class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var maintanance: Boolean?
        get() = prefs.getBoolean(MAINT, false)
        set(value) = prefs.edit().putBoolean(MAINT, value!!).apply()
}