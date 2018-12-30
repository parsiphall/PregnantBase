package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(dataModel: DataModel)

    @Query("SELECT * FROM DataModel")
    fun getData(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name")
    fun getCurrentData(name: String): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE :date BETWEEN fScrS AND fScrE AND fScrC <> 1 OR :date BETWEEN sScrS AND sScrE AND sScrC <> 1 OR :date BETWEEN tScrS AND tScrE AND tScrC <> 1")
    fun getScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE :date BETWEEN fScrS AND fScrE AND fScrC <> 1")
    fun getFScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE :date BETWEEN sScrS AND sScrE AND sScrC <> 1")
    fun getSScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE :date BETWEEN tScrS AND tScrE AND tScrC <> 1")
    fun getTScr(date: Long): List<DataModel>

    @Update
    fun updateData(dataModel: DataModel)

    @Delete
    fun deleteData(dataModel: DataModel)
}