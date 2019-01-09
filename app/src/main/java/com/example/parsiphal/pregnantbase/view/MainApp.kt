package com.example.parsiphal.pregnantbase.view

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
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

        val migration12 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN releaseDate TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyGender INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyWeight TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyHeight TEXT DEFAULT '' NOT NULL")
            }
        }

        mDataBase = Room
            .databaseBuilder(applicationContext, db::class.java, "pregnant_DB")
            .addMigrations(migration12)
            .allowMainThreadQueries()
            .build()
    }

    fun getDataBase(): db {
        return mDataBase!!
    }
}