package com.example.parsiphal.pregnantbase.view

import android.app.Application
import android.arch.persistence.room.Room
import com.example.parsiphal.pregnantbase.data.db

val DB: db by lazy {
    MainApp.mDataBase!!
}

class MainApp : Application() {

    companion object {
        var mDataBase: db? = null
    }

    override fun onCreate() {
        super.onCreate()
        mDataBase = Room
            .databaseBuilder(applicationContext, db::class.java, "pregnant_DB")
            .allowMainThreadQueries()
            .build()
    }

    fun getDataBase(): db {
        return mDataBase!!
    }
}