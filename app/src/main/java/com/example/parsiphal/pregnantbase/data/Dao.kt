package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(dataModel: DataModel)

    @Query("SELECT * FROM DataModel")
    fun getAllData(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` <> 1")
    fun getDataPregnant(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` = 1")
    fun getDataReleased(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name")
    fun getCurrentData(name: String): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN fScrC <> 1 AND (:date BETWEEN fScrS AND fScrE) " +
                "OR fScrC = 1 AND sScrC <> 1 AND (:date BETWEEN sScrS AND sScrE) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND (:date BETWEEN tScrS AND tScrE) " +
                "ELSE fScrC <> 1 AND (:date BETWEEN fScrS AND fScrE) " +
                "OR fScrC = 1 AND sScrC <> 1 AND (:date BETWEEN sScrSC AND sScrEC) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND (:date BETWEEN tScrSC AND tScrEC) END " +
                "AND `release` <> 1"
    )
    fun getScr(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE " +
                "(:date BETWEEN fScrS AND fScrE) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1"
    )
    fun getFScr(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN sScrS AND sScrE) AND sScrC <> 1 " +
                "ELSE (:date BETWEEN sScrSC AND sScrEC) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getSScr(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN tScrS AND tScrE) AND tScrC <> 1 " +
                "ELSE (:date BETWEEN tScrSC AND tScrEC) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getTScr(date: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE (fScrC <> 1 OR sScrC <> 1 OR tScrC <> 1) AND `release` <> 1")
    fun getScrAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC <> 1 AND `release` <> 1")
    fun getFScrAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC <> 1 AND `release` <> 1")
    fun getSScrAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND `release` <> 1")
    fun getTScrAll(): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN fScrC <> 1 AND ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "OR fScrC = 1 AND sScrC <> 1 AND ((:date1 BETWEEN sScrS AND sScrE) OR (:date2 BETWEEN sScrS AND sScrE)) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND ((:date1 BETWEEN tScrS AND tScrE) OR (:date2 BETWEEN tScrS AND tScrE)) " +
                "ELSE fScrC <> 1 AND ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "OR fScrC = 1 AND sScrC <> 1 AND ((:date1 BETWEEN sScrSC AND sScrEC) OR (:date2 BETWEEN sScrSC AND sScrEC)) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND ((:date1 BETWEEN tScrSC AND tScrEC) OR (:date2 BETWEEN tScrSC AND tScrEC)) END " +
                "AND `release` <> 1"
    )
    fun getScrRange(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1"
    )
    fun getFScrRange(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN sScrS AND sScrE) OR (:date2 BETWEEN sScrS AND sScrE)) AND sScrC <> 1 " +
                "ELSE ((:date1 BETWEEN sScrSC AND sScrEC) OR (:date2 BETWEEN sScrSC AND sScrEC)) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getSScrRange(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN tScrS AND tScrE) OR (:date2 BETWEEN tScrS AND tScrE)) AND tScrC <> 1 " +
                "ELSE ((:date1 BETWEEN tScrSC AND tScrEC) OR (:date2 BETWEEN tScrSC AND tScrEC)) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getTScrRange(date1: Long, date2: Long): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE CASE WHEN corr = 0 THEN ((:now - CAST(pm AS INTEGER))/604800000) = :weeks ELSE ((:now - CAST(fScrDate AS INTEGER))/604800000) = :weeks END")
    fun getWeeks(weeks: Int, now: Long): List<DataModel>

    @Update
    fun updateData(dataModel: DataModel)

    @Delete
    fun deleteData(dataModel: DataModel)
}