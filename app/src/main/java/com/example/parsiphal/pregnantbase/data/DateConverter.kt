package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun dateToString(date: Date): Long? {
        return date.time
    }

}