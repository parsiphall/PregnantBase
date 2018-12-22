package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import kotlinx.android.synthetic.main.fragment_details.*
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : MvpAppCompatFragment() {

    private var newData = false
    private lateinit var dataModel: DataModel
    lateinit var callBackActivity: MainView


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBackActivity = context as MainView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            dataModel = bundle.getSerializable("ITEM") as DataModel
        } else {
            dataModel = DataModel()
            newData = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!newData) {
            detail_fioEditText.isEnabled = false
            detail_birthdayEditText.isEnabled = false
            detail_phoneEditText.isEnabled = false
            detail_pmEditText.isEnabled = false
            detail_corrButton.visibility = View.VISIBLE
            detail_corrButton.setOnClickListener {
                YoYo.with(Techniques.Landing)
                    .duration(100)
                    .repeat(1)
                    .playOn(detail_corrButton)
                detail_13weeksTextView.visibility = View.VISIBLE
                detail_corrEditText.visibility = View.VISIBLE
                detail_corrSaveButton.visibility = View.VISIBLE
                detail_corrSaveButton.setOnClickListener {
                    corrData()
                }
            }
        }
        detail_fioEditText.setText(dataModel.name)
        detail_birthdayEditText.setText(dataModel.birthday)
        detail_phoneEditText.setText(dataModel.phone)
        detail_pmEditText.setText(dataModel.pm)
        detail_fScrS_TextView.text = dataModel.fScrS
        detail_fScrF_TextView.text = dataModel.fScrE
        detail_fScrCheck.isChecked = dataModel.fScrC
        detail_sScrS_TextView.text = dataModel.sScrS
        detail_sScrF_TextView.text = dataModel.sScrE
        detail_sScrCheck.isChecked = dataModel.sScrC
        detail_tScrS_TextView.text = dataModel.tScrS
        detail_tScrF_TextView.text = dataModel.tScrE
        detail_tScrCheck.isChecked = dataModel.tScrC
        detail_thirtyWeeksTextView.text = dataModel.thirtyWeeks
        detail_fortyWeeksTextView.text = dataModel.fortyWeeks
        if (detail_fScrCheck.isChecked) {
            detail_fScrCheck.setText(R.string.detail_Scr_check)
        } else {
            detail_fScrCheck.setText(R.string.detail_Scr_uncheck)
        }
        if (detail_sScrCheck.isChecked) {
            detail_sScrCheck.setText(R.string.detail_Scr_check)
        } else {
            detail_sScrCheck.setText(R.string.detail_Scr_uncheck)
        }
        if (detail_tScrCheck.isChecked) {
            detail_tScrCheck.setText(R.string.detail_Scr_check)
        } else {
            detail_tScrCheck.setText(R.string.detail_Scr_uncheck)
        }
        detail_button.setOnClickListener {
            YoYo.with(Techniques.Landing)
                .duration(100)
                .repeat(1)
                .playOn(detail_button)
            saveToBase()
        }
    }

    private fun saveToBase() {
        if (!newData) {
            dataModel.fScrC = detail_fScrCheck.isChecked
            dataModel.sScrC = detail_sScrCheck.isChecked
            dataModel.tScrC = detail_tScrCheck.isChecked
            DB.getDao().updateData(dataModel)
            callBackActivity.fragmentPlace(ListFragment())
        } else {
            dataModel.name = detail_fioEditText.text.toString()
            dataModel.birthday = detail_birthdayEditText.text.toString()
            dataModel.phone = detail_phoneEditText.text.toString()
            dataModel.pm = detail_pmEditText.text.toString()
            val pm = detail_pmEditText.text.toString()
            val pmDay = Integer.valueOf("${pm[0]}${pm[1]}")
            val pmMonth = Integer.valueOf("${pm[2]}${pm[3]}") - 1
            val pmYear = Integer.valueOf("20${pm[4]}${pm[5]}")
            val cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, pmYear)
            cal.set(Calendar.MONTH, pmMonth)
            cal.set(Calendar.DAY_OF_MONTH, pmDay)
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            cal.add(Calendar.DAY_OF_YEAR, 77)
            var date = cal.time
            dataModel.fScrS = sdf.format(date).toString()
            cal.add(Calendar.DAY_OF_YEAR, 20)
            date = cal.time
            dataModel.fScrE = sdf.format(date).toString()
            cal.add(Calendar.DAY_OF_YEAR, 29)
            date = cal.time
            dataModel.sScrS = sdf.format(date).toString()
            cal.add(Calendar.DAY_OF_YEAR, 20)
            date = cal.time
            dataModel.sScrE = sdf.format(date).toString()
            cal.add(Calendar.DAY_OF_YEAR, 64)
            date = cal.time
            dataModel.tScrS = sdf.format(date).toString()
            dataModel.thirtyWeeks = dataModel.tScrS
            cal.add(Calendar.DAY_OF_YEAR, 28)
            date = cal.time
            dataModel.tScrE = sdf.format(date).toString()
            cal.add(Calendar.DAY_OF_YEAR, 42)
            date = cal.time
            dataModel.fortyWeeks = sdf.format(date).toString()
            DB.getDao().addData(dataModel)
            callBackActivity.fragmentPlace(ListFragment())
        }
    }

    private fun corrData() {
        dataModel.fScrC = detail_fScrCheck.isChecked
        dataModel.sScrC = detail_sScrCheck.isChecked
        dataModel.tScrC = detail_tScrCheck.isChecked
        dataModel.fScrC = true
        val tw = detail_corrEditText.text.toString()
        val twDay = Integer.valueOf("${tw[0]}${tw[1]}")
        val twMonth = Integer.valueOf("${tw[2]}${tw[3]}") - 1
        val twYear = Integer.valueOf("20${tw[4]}${tw[5]}")
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, twYear)
        cal.set(Calendar.MONTH, twMonth)
        cal.set(Calendar.DAY_OF_MONTH, twDay)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        cal.add(Calendar.DAY_OF_YEAR, 36)
        var date = cal.time
        dataModel.sScrS = sdf.format(date).toString()
        cal.add(Calendar.DAY_OF_YEAR, 20)
        date = cal.time
        dataModel.sScrE = sdf.format(date).toString()
        cal.add(Calendar.DAY_OF_YEAR, 64)
        date = cal.time
        dataModel.tScrS = sdf.format(date).toString()
        dataModel.thirtyWeeks = dataModel.tScrS
        cal.add(Calendar.DAY_OF_YEAR, 28)
        date = cal.time
        dataModel.tScrE = sdf.format(date).toString()
        cal.add(Calendar.DAY_OF_YEAR, 42)
        date = cal.time
        dataModel.fortyWeeks = sdf.format(date).toString()
        DB.getDao().updateData(dataModel)
        callBackActivity.fragmentPlace(ListFragment())
    }
}
