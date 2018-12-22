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
    @ColumnInfo var fScrS: String = "-",
    @ColumnInfo var fScrE: String = "-",
    @ColumnInfo var sScrS: String = "-",
    @ColumnInfo var sScrE: String = "-",
    @ColumnInfo var tScrS: String = "-",
    @ColumnInfo var tScrE: String = "-",
    @ColumnInfo var fScrC: Boolean = false,
    @ColumnInfo var sScrC: Boolean = false,
    @ColumnInfo var tScrC: Boolean = false,
    @ColumnInfo var thirtyWeeks: String = "-",
    @ColumnInfo var fortyWeeks: String = "-"
) : Serializable