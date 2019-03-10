package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [DataModel::class], version = 4)
abstract class db: RoomDatabase() {
    abstract fun getDao(): com.example.parsiphal.pregnantbase.data.Dao
}