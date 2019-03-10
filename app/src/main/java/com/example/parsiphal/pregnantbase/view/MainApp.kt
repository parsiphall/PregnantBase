package com.example.parsiphal.pregnantbase.view

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.content.Context
import com.example.parsiphal.pregnantbase.data.DISTRICT
import com.example.parsiphal.pregnantbase.data.Preferences
import com.example.parsiphal.pregnantbase.data.db

const val DB_NAME = "pregnant_DB"
const val DB_SHM = "pregnant_DB-shm"
const val DB_WAL = "pregnant_DB-wal"

val prefs: Preferences by lazy {
    MainApp.prefs!!
}

val DB: db by lazy {
    MainApp.mDataBase!!
}

class MainApp : Application() {

    companion object {
        var prefs: Preferences? = null
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

        val migration23 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN comment TEXT DEFAULT '' NOT NULL")
            }
        }

        val migration34 = object : Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN district INTEGER DEFAULT 0 NOT NULL")
            }
        }

        prefs = Preferences(applicationContext)

        mDataBase = Room
            .databaseBuilder(applicationContext, db::class.java, DB_NAME)
            .addMigrations(migration12, migration23, migration34)
            .build()

    }
}

//TODO 2 участка на 1м компе(разные БД)
//TODO просмотр всех БД для УЗИ

//TODO рассчитать срок родов от даты
//TODO поиск по рискам
//TODO корректировка п/м и I скрининга
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO иногда нужна корректировка от II скр.
//TODO точный рассчёт возраста