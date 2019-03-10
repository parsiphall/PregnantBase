package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

@Dao
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(dataModel: DataModel)

    @Query("SELECT * FROM DataModel")
    fun getAllDataAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE district LIKE :district")
    fun getAllDataDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` <> 1")
    fun getDataPregnantAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` <> 1 AND district LIKE :district")
    fun getDataPregnantDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` = 1")
    fun getDataReleasedAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE `release` = 1 AND district LIKE :district")
    fun getDataReleasedDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name")
    fun getCurrentDataAll(name: String): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name AND district LIKE :district")
    fun getCurrentDataDistr(name: String, district: Int): List<DataModel>

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
    fun getScrAll(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN fScrC <> 1 AND (:date BETWEEN fScrS AND fScrE) " +
                "OR fScrC = 1 AND sScrC <> 1 AND (:date BETWEEN sScrS AND sScrE) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND (:date BETWEEN tScrS AND tScrE) " +
                "ELSE fScrC <> 1 AND (:date BETWEEN fScrS AND fScrE) " +
                "OR fScrC = 1 AND sScrC <> 1 AND (:date BETWEEN sScrSC AND sScrEC) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND (:date BETWEEN tScrSC AND tScrEC) END " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getScrDistr(date: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE " +
                "(:date BETWEEN fScrS AND fScrE) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1"
    )
    fun getFScrAll(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE " +
                "(:date BETWEEN fScrS AND fScrE) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getFScrDistr(date: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN sScrS AND sScrE) AND sScrC <> 1 " +
                "ELSE (:date BETWEEN sScrSC AND sScrEC) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getSScrAll(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN sScrS AND sScrE) AND sScrC <> 1 " +
                "ELSE (:date BETWEEN sScrSC AND sScrEC) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getSScrDistr(date: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN tScrS AND tScrE) AND tScrC <> 1 " +
                "ELSE (:date BETWEEN tScrSC AND tScrEC) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getTScrAll(date: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (:date BETWEEN tScrS AND tScrE) AND tScrC <> 1 " +
                "ELSE (:date BETWEEN tScrSC AND tScrEC) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getTScrDistr(date: Long, district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE (fScrC <> 1 OR sScrC <> 1 OR tScrC <> 1) AND `release` <> 1")
    fun getScrAllAll(): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE (fScrC <> 1 OR sScrC <> 1 OR tScrC <> 1) " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getScrAllDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC <> 1 AND `release` <> 1")
    fun getFScrAllAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC <> 1 AND `release` <> 1 AND district LIKE :district")
    fun getFScrAllDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC <> 1 AND `release` <> 1")
    fun getSScrAllAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC <> 1 AND `release` <> 1 AND district LIKE :district")
    fun getSScrAllDistr(district: Int): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND `release` <> 1")
    fun getTScrAllAll(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND `release` <> 1 AND district LIKE :district")
    fun getTScrAllDistr(district: Int): List<DataModel>

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
    fun getScrRangeAll(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN fScrC <> 1 AND ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "OR fScrC = 1 AND sScrC <> 1 AND ((:date1 BETWEEN sScrS AND sScrE) OR (:date2 BETWEEN sScrS AND sScrE)) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND ((:date1 BETWEEN tScrS AND tScrE) OR (:date2 BETWEEN tScrS AND tScrE)) " +
                "ELSE fScrC <> 1 AND ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "OR fScrC = 1 AND sScrC <> 1 AND ((:date1 BETWEEN sScrSC AND sScrEC) OR (:date2 BETWEEN sScrSC AND sScrEC)) " +
                "OR fScrC = 1 AND sScrC = 1 AND tScrC <> 1 AND ((:date1 BETWEEN tScrSC AND tScrEC) OR (:date2 BETWEEN tScrSC AND tScrEC)) END " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getScrRangeDistr(date1: Long, date2: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1"
    )
    fun getFScrRangeAll(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE ((:date1 BETWEEN fScrS AND fScrE) OR (:date2 BETWEEN fScrS AND fScrE)) " +
                "AND fScrC <> 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getFScrRangeDistr(date1: Long, date2: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN sScrS AND sScrE) OR (:date2 BETWEEN sScrS AND sScrE)) AND sScrC <> 1 " +
                "ELSE ((:date1 BETWEEN sScrSC AND sScrEC) OR (:date2 BETWEEN sScrSC AND sScrEC)) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getSScrRangeAll(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN sScrS AND sScrE) OR (:date2 BETWEEN sScrS AND sScrE)) AND sScrC <> 1 " +
                "ELSE ((:date1 BETWEEN sScrSC AND sScrEC) OR (:date2 BETWEEN sScrSC AND sScrEC)) AND sScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getSScrRangeDistr(date1: Long, date2: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN tScrS AND tScrE) OR (:date2 BETWEEN tScrS AND tScrE)) AND tScrC <> 1 " +
                "ELSE ((:date1 BETWEEN tScrSC AND tScrEC) OR (:date2 BETWEEN tScrSC AND tScrEC)) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1"
    )
    fun getTScrRangeAll(date1: Long, date2: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN ((:date1 BETWEEN tScrS AND tScrE) OR (:date2 BETWEEN tScrS AND tScrE)) AND tScrC <> 1 " +
                "ELSE ((:date1 BETWEEN tScrSC AND tScrEC) OR (:date2 BETWEEN tScrSC AND tScrEC)) AND tScrC <> 1 END " +
                "AND fScrC = 1 " +
                "AND sScrC = 1 " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getTScrRangeDistr(date1: Long, date2: Long, district: Int): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (((:now - CAST(pm AS INTEGER))/604800000) = :weeks) " +
                "ELSE (((:now - CAST(fScrDate AS INTEGER))/604800000) + fScrTimeWeeks + " +
                "(CASE WHEN (((((:now - CAST(fScrDate AS INTEGER))/86400000)%7) + fScrTimeDays) > 6) THEN 1 ELSE 0 END) = :weeks) END " +
                "AND `release` <> 1"
    )
    fun getWeeksAll(weeks: Int, now: Long): List<DataModel>

    @Query(
        "SELECT * FROM DataModel WHERE CASE WHEN corr = 0 " +
                "THEN (((:now - CAST(pm AS INTEGER))/604800000) = :weeks) " +
                "ELSE (((:now - CAST(fScrDate AS INTEGER))/604800000) + fScrTimeWeeks + " +
                "(CASE WHEN (((((:now - CAST(fScrDate AS INTEGER))/86400000)%7) + fScrTimeDays) > 6) THEN 1 ELSE 0 END) = :weeks) END " +
                "AND `release` <> 1 " +
                "AND district LIKE :district"
    )
    fun getWeeksDistr(weeks: Int, now: Long, district: Int): List<DataModel>

    @Update
    fun updateData(dataModel: DataModel)

    @Delete
    fun deleteData(dataModel: DataModel)
}