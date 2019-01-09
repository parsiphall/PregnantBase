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
    @ColumnInfo var fortyWeeks: Long = 0L,
    @ColumnInfo var risk: Int = 0,
    @ColumnInfo var riskText: String = "",
    @ColumnInfo var multiplicity: Boolean = false,
    @ColumnInfo var release: Boolean = false,
    @ColumnInfo var corr: Boolean = false,
    @ColumnInfo var fScrDate: String = "",
    @ColumnInfo var fScrTimeWeeks: String = "",
    @ColumnInfo var fScrTimeDays: String = "",
    @ColumnInfo var sScrSC: Long = 0L,
    @ColumnInfo var sScrEC: Long = 0L,
    @ColumnInfo var tScrSC: Long = 0L,
    @ColumnInfo var tScrEC: Long = 0L,
    @ColumnInfo var thirtyWeeksC: Long = 0L,
    @ColumnInfo var fortyWeeksC: Long = 0L,
    @ColumnInfo var releaseDate: String = "",
    @ColumnInfo var babyGender: Int = 0,
    @ColumnInfo var babyWeight: String = "",
    @ColumnInfo var babyHeight: String = ""
) : Serializable