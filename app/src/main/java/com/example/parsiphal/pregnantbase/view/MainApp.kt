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

//TODO экспорт/импорт БД
//TODO перевод БД на Rx

//TODO перехват отсутствия ПМ при добавлении
//TODO PDF -> список поиска(ФИО, даты, №скр)
//TODO PDF -> карточка
//TODO риски в списке сократить до буквы
//TODO рассчитать срок родов от даты
//TODO поле комментарий, скрытое поле, цветовая идентификация на наличие записи
//TODO кнопка "+"
//TODO EditText ярче
//TODO поиск по >= 38 недель
//TODO шаблоны в цифровые поля
//TODO поиск по рискам
//TODO не закрывать карточку после создания или корректировки
//TODO корректировка п/м и I скрининга
//TODO перехват отсутствия ПМ при добавлении
//TODO кнопки "сохранить", "корректировать" и "закрыть" в OptionsMenu
//TODO убрать проведён/не проведён
//TODO в корректировке переход по Enter
//TODO переходы по первым EditText
//TODO не один ребёнок(4)
//TODO вес, прибавка; календарь
//TODO синхронизация БД на 2х устр.
//TODO 2 участка на 1м компе
//TODO иногда нужна корректировка от II скр.

