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

        val migration23 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE DataModel ADD COLUMN comment TEXT DEFAULT '' NOT NULL")
            }
        }

        mDataBase = Room
            .databaseBuilder(applicationContext, db::class.java, "pregnant_DB")
            .addMigrations(migration12, migration23)
            .build()
    }
}

//TODO экспорт/импорт БД
//TODO перевод БД на Rx

//TODO поиск по периоду скрининга
//TODO рассчитать срок родов от даты
//TODO поиск по >= 38 недель
//TODO поиск по рискам
//TODO корректировка п/м и I скрининга
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO синхронизация БД на 2х устр.
//TODO 2 участка на 1м компе
//TODO иногда нужна корректировка от II скр.