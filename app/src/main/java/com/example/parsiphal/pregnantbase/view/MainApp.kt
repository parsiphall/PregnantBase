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

//TODO пункт "удалить" с подтверждением
//TODO поиск по >= 38 недель
//TODO перевод БД на Rx
//TODO шаблоны в цифровые поля
//TODO не один ребёнок(4)
//TODO поле комментарий
//TODO PDF -> список поиска(ФИО, даты, №скр)
//TODO PDF -> карточка
//TODO кнопка "+"
//TODO EditText ярче
//TODO не закрывать карточку после создания или корректировки
//TODO № первого не сделанного скр. с периодом в список
//TODO риски в списке сократить до буквы
//TODO поиск по рискам
//TODO Navigation Drawer + пункт "родившие"
//TODO убрать столбик "родила"
//TODO в корректировке переход по Enter
//TODO перехват отсутствия ПМ при добавлении
//TODO кнопки "сохранить", "корректировать" и "закрыть" в OptionsMenu
//TODO убрать проведён/не проведён
//TODO переходы по первым EditText
//TODO экспорт/импорт БД