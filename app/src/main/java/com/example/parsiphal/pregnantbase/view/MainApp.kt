package com.example.parsiphal.pregnantbase.view

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
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

        prefs = Preferences(applicationContext)

        val migration12 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN releaseDate TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyGender INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyWeight TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyHeight TEXT DEFAULT '' NOT NULL")
            }
        }

        val migration23 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN comment TEXT DEFAULT '' NOT NULL")
            }
        }

        mDataBase = if (applicationContext.getDatabasePath(DB_NAME).exists()) {
            Room
                .databaseBuilder(applicationContext, db::class.java, DB_NAME)
                .addMigrations(migration12, migration23)
                .build()
        } else {
            Room
                .databaseBuilder(applicationContext, db::class.java, DB_NAME)
                .build()
        }
    }
}

//TODO рассчитать срок родов от даты
//TODO поиск по рискам
//TODO корректировка п/м и I скрининга
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO синхронизация БД на 2х устр.
//TODO 2 участка на 1м компе(разные БД)
//TODO иногда нужна корректировка от II скр.