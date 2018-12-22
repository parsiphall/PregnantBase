package com.example.parsiphal.pregnantbase.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class DataModel(
    @PrimaryKey(autoGenerate = true) @ColumnInfo var id: Long = 0,
    @ColumnInfo var name: String = "",
    @ColumnInfo var birthday: String = "",
    @ColumnInfo var phone: String = "",
    @ColumnInfo var pm: String = "",
    @ColumnInfo var fScrS: Long = 0L,
    @ColumnInfo var fScrE: Long = 0L,
    @ColumnInfo var sScrS: Long = 0L,
    @ColumnInfo var sScrE: Long = 0L,
    @ColumnInfo var tScrS: Long = 0L,
    @ColumnInfo var tScrE: Long = 0L,
    @ColumnInfo var fScrC: Boolean = false,
    @ColumnInfo var sScrC: Boolean = false,
    @ColumnInfo var tScrC: Boolean = false,
    @ColumnInfo var thirtyWeeks: Long = 0L,
    @ColumnInfo var fortyWeeks: Long = 0L
) : Serializable