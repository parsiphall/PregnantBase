package com.example.parsiphal.pregnantbase.data

import android.content.Context

const val PREFS_FILENAME = "com.example.parsiphal.pregnantbase.prefs"
const val DISTRICT = "district"

class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var district: Int
        get() = prefs.getInt(DISTRICT, 0)
        set(value) = prefs.edit().putInt(DISTRICT, value).apply()
}