package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(dataModel: DataModel)

    @Query("SELECT * FROM DataModel WHERE `release` <> 1")
    fun getDataPregnant(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` = 1")
    fun getDataReleased(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name")
    fun getCurrentData(name: String): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN :date BETWEEN fScrS AND fScrE AND fScrC <> 1 OR :date BETWEEN sScrS AND sScrE AND sScrC <> 1 OR :date BETWEEN tScrS AND tScrE AND tScrC <> 1 ELSE :date BETWEEN fScrS AND fScrE AND fScrC <> 1 OR :date BETWEEN sScrSC AND sScrEC AND sScrC <> 1 OR :date BETWEEN tScrSC AND tScrEC AND tScrC <> 1 END")
    fun getScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE :date BETWEEN fScrS AND fScrE AND fScrC <> 1")
    fun getFScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN :date BETWEEN sScrS AND sScrE AND sScrC <> 1 ELSE :date BETWEEN sScrSC AND sScrEC AND sScrC <> 1 END")
    fun getSScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN :date BETWEEN tScrS AND tScrE AND tScrC <> 1 ELSE :date BETWEEN tScrSC AND tScrEC AND tScrC <> 1 END")
    fun getTScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN (:weekStart BETWEEN fScrS AND fScrE AND fScrC <> 1 OR :weekEnd BETWEEN fScrS AND fScrE AND fScrC <> 1) OR (:weekStart BETWEEN sScrS AND sScrE AND sScrC <> 1 OR :weekEnd BETWEEN sScrS AND sScrE AND sScrC <> 1) OR (:weekStart BETWEEN tScrS AND tScrE AND tScrC <> 1 OR :weekEnd BETWEEN tScrS AND tScrE AND tScrC <> 1) ELSE (:weekStart BETWEEN fScrS AND fScrE AND fScrC <> 1 OR :weekEnd BETWEEN fScrS AND fScrE AND fScrC <> 1) OR (:weekStart BETWEEN sScrSC AND sScrEC AND sScrC <> 1 OR :weekEnd BETWEEN sScrSC AND sScrEC AND sScrC <> 1) OR (:weekStart BETWEEN tScrSC AND tScrEC AND tScrC <> 1 OR :weekEnd BETWEEN tScrSC AND tScrEC AND tScrC <> 1) END")
    fun getScrWeek(weekStart: Long, weekEnd: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE (:weekStart BETWEEN fScrS AND fScrE AND fScrC <> 1) OR (:weekEnd BETWEEN fScrS AND fScrE AND fScrC <> 1)")
    fun getFScrWeek(weekStart: Long, weekEnd: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN :weekStart BETWEEN sScrS AND sScrE AND sScrC <> 1 OR :weekEnd BETWEEN sScrS AND sScrE AND sScrC <> 1 ELSE :weekStart BETWEEN sScrSC AND sScrEC AND sScrC <> 1 OR :weekEnd BETWEEN sScrSC AND sScrEC AND sScrC <> 1 END")
    fun getSScrWeek(weekStart: Long, weekEnd: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN :weekStart BETWEEN tScrS AND tScrE AND tScrC <> 1 OR :weekEnd BETWEEN tScrS AND tScrE AND tScrC <> 1 ELSE :weekStart BETWEEN tScrSC AND tScrEC AND tScrC <> 1 OR :weekEnd BETWEEN tScrSC AND tScrEC AND tScrC <> 1 END")
    fun getTScrWeek(weekStart: Long, weekEnd: Long): List<DataModel>

    @Update
    fun updateData(dataModel: DataModel)

    @Delete
    fun deleteData(dataModel: DataModel)
}