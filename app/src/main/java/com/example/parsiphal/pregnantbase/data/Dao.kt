package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.*
import android.arch.persistence.room.Dao

@Dao
@TypeConverters(DateConverter::class)
interface Dao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(dataModel: DataModel)

    @Query("SELECT * FROM DataModel")
    fun getData(): List<DataModel>

    @Query("SELECT * FROM DataModel WHERE name LIKE :name")
    fun getCurrentData(name: String): List<DataModel>

    @Update
    fun updateData(dataModel: DataModel)

    @Delete
    fun deleteData(dataModel: DataModel)
}