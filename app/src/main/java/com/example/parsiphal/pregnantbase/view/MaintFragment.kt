package com.example.parsiphal.pregnantbase.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import kotlinx.android.synthetic.main.fragment_maint.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class MaintFragment : MvpAppCompatFragment() {

    private var items: List<DataModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            items = DB.getDao().getAllData()
        }
        maint_birthday.setOnClickListener {
            GlobalScope.launch {
                maintBirthday()
            }
        }
        maint_pm.setOnClickListener {
            GlobalScope.launch {
                maintPM()
            }
        }
        maint_fScrDate.setOnClickListener {
            GlobalScope.launch {
                maintFScrDate()
            }
        }
    }

    private suspend fun maintBirthday() {
        for (position in items) {
            val bd = position.birthday
            val newBD: String =
                when (bd.length) {
                    10 -> {
                        val cal = Calendar.getInstance()
                        cal.set(
                            Calendar.YEAR,
                            Integer.valueOf("${bd[6]}${bd[7]}${bd[8]}${bd[9]}")
                        )
                        cal.set(Calendar.MONTH, Integer.valueOf("${bd[3]}${bd[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${bd[0]}${bd[1]}"))
                        cal.timeInMillis.toString()
                    }
                    8 -> {
                        val cal = Calendar.getInstance()
                        if ("${bd[6]}${bd[7]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${bd[6]}${bd[7]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${bd[6]}${bd[7]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${bd[3]}${bd[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${bd[0]}${bd[1]}"))
                        cal.timeInMillis.toString()
                    }
                    6 -> {
                        val cal = Calendar.getInstance()
                        if ("${bd[4]}${bd[5]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${bd[4]}${bd[5]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${bd[4]}${bd[5]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${bd[2]}${bd[3]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${bd[0]}${bd[1]}"))
                        cal.timeInMillis.toString()
                    }
                    else -> bd
                }
            position.birthday = newBD
            DB.getDao().updateData(position)
        }
        MainScope().launch {
            Toast.makeText(context, "birthday converted",Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun maintPM() {
        for (position in items) {
            val pm = position.pm
            val newPM: String =
                when (pm.length) {
                    10 -> {
                        val cal = Calendar.getInstance()
                        cal.set(
                            Calendar.YEAR,
                            Integer.valueOf("${pm[6]}${pm[7]}${pm[8]}${pm[9]}")
                        )
                        cal.set(Calendar.MONTH, Integer.valueOf("${pm[3]}${pm[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${pm[0]}${pm[1]}"))
                        cal.timeInMillis.toString()
                    }
                    8 -> {
                        val cal = Calendar.getInstance()
                        if ("${pm[6]}${pm[7]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${pm[6]}${pm[7]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${pm[6]}${pm[7]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${pm[3]}${pm[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${pm[0]}${pm[1]}"))
                        cal.timeInMillis.toString()
                    }
                    6 -> {
                        val cal = Calendar.getInstance()
                        if ("${pm[4]}${pm[5]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${pm[4]}${pm[5]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${pm[4]}${pm[5]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${pm[2]}${pm[3]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${pm[0]}${pm[1]}"))
                        cal.timeInMillis.toString()
                    }
                    else -> pm
                }
            position.pm = newPM
            DB.getDao().updateData(position)
        }
        MainScope().launch {
            Toast.makeText(context, "pm converted",Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun maintFScrDate() {
        for (position in items) {
            val fScrDate = position.fScrDate
            val newFScrDate: String =
                when (fScrDate.length) {
                    10 -> {
                        val cal = Calendar.getInstance()
                        cal.set(
                            Calendar.YEAR,
                            Integer.valueOf("${fScrDate[6]}${fScrDate[7]}${fScrDate[8]}${fScrDate[9]}")
                        )
                        cal.set(Calendar.MONTH, Integer.valueOf("${fScrDate[3]}${fScrDate[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${fScrDate[0]}${fScrDate[1]}"))
                        cal.timeInMillis.toString()
                    }
                    8 -> {
                        val cal = Calendar.getInstance()
                        if ("${fScrDate[6]}${fScrDate[7]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${fScrDate[6]}${fScrDate[7]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${fScrDate[6]}${fScrDate[7]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${fScrDate[3]}${fScrDate[4]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${fScrDate[0]}${fScrDate[1]}"))
                        cal.timeInMillis.toString()
                    }
                    6 -> {
                        val cal = Calendar.getInstance()
                        if ("${fScrDate[4]}${fScrDate[5]}".toInt() < 50) {
                            cal.set(Calendar.YEAR, Integer.valueOf("20${fScrDate[4]}${fScrDate[5]}"))
                        } else {
                            cal.set(Calendar.YEAR, Integer.valueOf("19${fScrDate[4]}${fScrDate[5]}"))
                        }
                        cal.set(Calendar.MONTH, Integer.valueOf("${fScrDate[2]}${fScrDate[3]}") - 1)
                        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${fScrDate[0]}${fScrDate[1]}"))
                        cal.timeInMillis.toString()
                    }
                    else -> fScrDate
                }
            position.fScrDate = newFScrDate
            DB.getDao().updateData(position)
        }
        MainScope().launch {
            Toast.makeText(context, "fScrDate converted",Toast.LENGTH_LONG).show()
        }
    }
}
