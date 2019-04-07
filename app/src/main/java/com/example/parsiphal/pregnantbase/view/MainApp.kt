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

        val mig1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN releaseDate TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyGender INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyWeight TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN babyHeight TEXT DEFAULT '' NOT NULL")
            }
        }

        val mig2to3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN comment TEXT DEFAULT '' NOT NULL")
            }
        }

        val mig3to4 = object : Migration(3,4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN district INTEGER DEFAULT 0 NOT NULL")
            }
        }

        val mig4to5 = object : Migration(4,5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN sScrDate TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE DataModel ADD COLUMN tScrDate TEXT DEFAULT '' NOT NULL")
            }
        }

        prefs = Preferences(applicationContext)

        mDataBase = Room
            .databaseBuilder(applicationContext, db::class.java, DB_NAME)
            .addMigrations(mig1to2, mig2to3, mig3to4, mig4to5)
            .build()

    }
}

//TODO пересчитать имеющиеся данные:
//                                      срок на сегодня
//                                      начало и конец III скр.
//TODO нормальная генерация .pdf для поиска
//TODO рассчитать срок родов от даты
//TODO поиск по рискам
//TODO корректировка п/м и I скрининга
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO иногда нужна корректировка от II скр.