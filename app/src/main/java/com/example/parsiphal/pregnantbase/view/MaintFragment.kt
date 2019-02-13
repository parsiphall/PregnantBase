package com.example.parsiphal.pregnantbase.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.view.ContextThemeWrapper
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
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.*

class MaintFragment : MvpAppCompatFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maint, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        maint_export.setOnClickListener {
            GlobalScope.launch {
                exportDB()
            }
        }
        maint_import.setOnClickListener {
            GlobalScope.launch {
                importDB()
            }
        }
    }

    private suspend fun exportDB() {
        val sd =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/PregnantBase/Export")
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDB = File(context?.getDatabasePath(DB_NAME).toString())
        val currentSHM = File(context?.getDatabasePath(DB_SHM).toString())
        val currentWAL = File(context?.getDatabasePath(DB_WAL).toString())
        val backupDB = File(sd.absolutePath + "/backupDB")
        val backupSHM = File(sd.absolutePath + "/backupSHM")
        val backupWAL = File(sd.absolutePath + "/backupWAL")
        if (!sd.exists()) {
            sd.mkdirs()
        }
        if (!backupDB.exists()) {
            backupDB.createNewFile()
            backupSHM.createNewFile()
            backupWAL.createNewFile()
        }
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "DB Exported!", Toast.LENGTH_LONG).show()
            }
            source = FileInputStream(currentSHM).channel
            destination = FileOutputStream(backupSHM).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "SHM Exported!", Toast.LENGTH_LONG).show()
            }
            source = FileInputStream(currentWAL).channel
            destination = FileOutputStream(backupWAL).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "WAL Exported!", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            MainScope().launch {
                Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
            }
        } finally {
            source?.close()
            destination?.close()
        }
    }

    private suspend fun importDB() {
        val sd =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString() + "/PregnantBase/Export")
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val newDB = File(sd, "/backupDB")
        val newSHM = File(sd, "/backupSHM")
        val newWAL = File(sd, "/backupWAL")
        val oldDB = File(context?.getDatabasePath(DB_NAME).toString())
        val oldSHM = File(context?.getDatabasePath(DB_SHM).toString())
        val oldWAL = File(context?.getDatabasePath(DB_WAL).toString())
        try {
            source = FileInputStream(newDB).channel
            destination = FileOutputStream(oldDB).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "DB Imported!", Toast.LENGTH_LONG).show()
            }
            source = FileInputStream(newSHM).channel
            destination = FileOutputStream(oldSHM).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "SHM Imported!", Toast.LENGTH_LONG).show()
            }
            source = FileInputStream(newWAL).channel
            destination = FileOutputStream(oldWAL).channel
            destination!!.transferFrom(source, 0, source!!.size())
            MainScope().launch {
                Toast.makeText(context, "WAL Imported!", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            MainScope().launch {
                Toast.makeText(context, "Error!", Toast.LENGTH_LONG).show()
            }
        } finally {
            source?.close()
            destination?.close()
        }
    }
}
