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

        prefs = Preferences(applicationContext)

        mDataBase = Room
                .databaseBuilder(applicationContext, db::class.java, DB_NAME)
                .build()

    }
}
//TODO не верный расчет срока
//TODO 2 участка на 1м компе(разные БД)

//TODO рассчитать срок родов от даты
//TODO поиск по рискам
//TODO корректировка п/м и I скрининга
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO иногда нужна корректировка от II скр.
//TODO точный рассчёт возраста