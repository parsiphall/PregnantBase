package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment
import android.os.Environment
import android.view.*
import android.widget.Toast

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import kotlinx.android.synthetic.main.fragment_details.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : MvpAppCompatFragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        menu.findItem(R.id.menu_detail_save).isVisible = true
        menu.findItem(R.id.menu_detail_back).isVisible = true
        if (!newData) {
            menu.findItem(R.id.menu_detail_pdf).isVisible = true
            menu.findItem(R.id.menu_detail_edit).isVisible = true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_detail_pdf -> {
                generatePDF(dataModel.name)
                return true
            }
            R.id.menu_detail_save -> {
                saveToBase()
                hideKeyboard(detail_root_R)
                return true
            }
            R.id.menu_detail_edit -> {
                correctData()
                hideKeyboard(detail_root_R)
                return true
            }
            R.id.menu_detail_back -> {
                fragmentManager?.popBackStackImmediate()
            }
        }
        return false
    }

    private var newData = false
    private lateinit var dataModel: DataModel
    private lateinit var callBackActivity: MainView


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBackActivity = context as MainView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        if (!newData) {
            detail_fioEditText.isEnabled = false
            detail_birthdayEditText.isEnabled = false
            detail_phoneEditText.isEnabled = false
            detail_pmEditText.isEnabled = false
            detail_commentEditText.isEnabled = false
            if (dataModel.corr) {
                detail_corrTextView.visibility = View.VISIBLE
                detail_fScrC.visibility = View.VISIBLE
                detail_sScrC.visibility = View.VISIBLE
                detail_tScrC.visibility = View.VISIBLE
                detail_weeksC.visibility = View.VISIBLE
                detail_fScrCheck.isClickable = false
                detail_sScrCheck.isClickable = false
                detail_tScrCheck.isClickable = false

                val fScrDate = dataModel.fScrDate
                val calFScr = Calendar.getInstance()
                val fScrDay = Integer.valueOf("${fScrDate[0]}${fScrDate[1]}")
                val fScrMonth = Integer.valueOf("${fScrDate[2]}${fScrDate[3]}") - 1
                val fScrYear = Integer.valueOf("20${fScrDate[4]}${fScrDate[5]}")
                calFScr.set(Calendar.YEAR, fScrYear)
                calFScr.set(Calendar.MONTH, fScrMonth)
                calFScr.set(Calendar.DAY_OF_MONTH, fScrDay)
                val format = calFScr.timeInMillis
                val fScrCD = sdf.format(format)
                val fScrCW = "${dataModel.fScrTimeWeeks} ${resources.getString(R.string.weeks)}"
                val fScrCd = "${dataModel.fScrTimeDays} ${resources.getString(R.string.days)}"
                val fScrC = "$fScrCD $fScrCW $fScrCd"
                detail_fScrDate_TextView.text = fScrC
                detail_sScrSC_TextView.text = sdf.format(dataModel.sScrSC)
                detail_sScrFC_TextView.text = sdf.format(dataModel.sScrEC)
                detail_tScrSC_TextView.text = sdf.format(dataModel.tScrSC)
                detail_tScrFC_TextView.text = sdf.format(dataModel.tScrEC)
                detail_thirtyWeeksCTextView.text = sdf.format(dataModel.thirtyWeeksC)
                detail_fortyWeeksCTextView.text = sdf.format(dataModel.fortyWeeksC)
            }
            if (dataModel.release) {
                detail_baby.visibility = View.VISIBLE
                detail_releaseDateEditText.isEnabled = false
                detail_babyGenderSpinner.isEnabled = false
                detail_babyWeightEditText.isEnabled = false
                detail_babyHeightEditText.isEnabled = false

                detail_releaseDateEditText.setText(dataModel.releaseDate)
                detail_babyGenderSpinner.setSelection(dataModel.babyGender)
                detail_babyWeightEditText.setText(dataModel.babyWeight)
                detail_babyHeightEditText.setText(dataModel.babyHeight)
            }
            todayTime(dataModel)
        }
        detail_fioEditText.setText(dataModel.name)
        detail_birthdayEditText.setText(dataModel.birthday)
        detail_phoneEditText.setText(dataModel.phone)
        detail_pmEditText.setText(dataModel.pm)
        detail_fScrS_TextView.text = sdf.format(dataModel.fScrS)
        detail_fScrF_TextView.text = sdf.format(dataModel.fScrE)
        detail_fScrCheck.isChecked = dataModel.fScrC
        detail_sScrS_TextView.text = sdf.format(dataModel.sScrS)
        detail_sScrF_TextView.text = sdf.format(dataModel.sScrE)
        detail_sScrCheck.isChecked = dataModel.sScrC
        detail_sScrCCheck.isChecked = detail_sScrCheck.isChecked
        detail_tScrS_TextView.text = sdf.format(dataModel.tScrS)
        detail_tScrF_TextView.text = sdf.format(dataModel.tScrE)
        detail_tScrCheck.isChecked = dataModel.tScrC
        detail_tScrCCheck.isChecked = detail_tScrCheck.isChecked
        detail_thirtyWeeksTextView.text = sdf.format(dataModel.thirtyWeeks)
        detail_fortyWeeksTextView.text = sdf.format(dataModel.fortyWeeks)

        detail_releaseCheckBox.isChecked = dataModel.release
        detail_multiplicityCheckBox.isChecked = dataModel.multiplicity
        detail_riskSpinner.setSelection(dataModel.risk)

        detail_commentEditText.setText(dataModel.comment)
        detail_age.text = calculateAge(dataModel.birthday)

        if (detail_releaseCheckBox.isChecked) {
            detail_releaseCheckBox.setText(R.string.release)
        } else {
            detail_releaseCheckBox.setText(R.string.notRelease)
        }

        detail_releaseCheckBox.setOnCheckedChangeListener { _, _ ->
            detail_baby.visibility = View.VISIBLE
        }
    }

    private fun correctData() {
        detail_fioEditText.isEnabled = true
        detail_birthdayEditText.isEnabled = true
        detail_phoneEditText.isEnabled = true
        detail_releaseDateEditText.isEnabled = true
        detail_babyGenderSpinner.isEnabled = true
        detail_babyWeightEditText.isEnabled = true
        detail_babyHeightEditText.isEnabled = true
        detail_commentEditText.isEnabled = true
        if (!dataModel.corr) {
            detail_13weeksLinear.visibility = View.VISIBLE
            detail_corrSaveButton.setOnClickListener {
                hideKeyboard(it)
                corrData()
            }
        }
    }

    private fun generatePDF(name: String) {
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = document.startPage(pageInfo)
        detail_root_S.draw(page.canvas)
        document.finishPage(page)
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/PregnantBase")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val targetPDF = "${dir.absolutePath}/$name.pdf"
        val filePath = File(targetPDF)
        try {
            val outputStream = FileOutputStream(filePath)
            document.writeTo(outputStream)
            Toast.makeText(context, "Файл $name.pdf сохранён", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
        }
        document.close()
    }

    private fun saveToBase() {
        if (detail_pmEditText.text.toString().length == 6) {
            dataModel.name = detail_fioEditText.text.toString()
            dataModel.birthday = detail_birthdayEditText.text.toString()
            dataModel.phone = detail_phoneEditText.text.toString()
            dataModel.release = detail_releaseCheckBox.isChecked
            dataModel.multiplicity = detail_multiplicityCheckBox.isChecked
            dataModel.risk = detail_riskSpinner.selectedItemPosition
            dataModel.riskText = detail_riskSpinner.selectedItem.toString()
            dataModel.releaseDate = detail_releaseDateEditText.text.toString()
            dataModel.babyGender = detail_babyGenderSpinner.selectedItemPosition
            dataModel.babyWeight = detail_babyWeightEditText.text.toString()
            dataModel.babyHeight = detail_babyHeightEditText.text.toString()
            dataModel.comment = detail_commentEditText.text.toString()
            if (!newData) {
                if (dataModel.corr) {
                    dataModel.sScrC = detail_sScrCCheck.isChecked
                    dataModel.tScrC = detail_tScrCCheck.isChecked
                } else {
                    dataModel.fScrC = detail_fScrCheck.isChecked
                    dataModel.sScrC = detail_sScrCheck.isChecked
                    dataModel.tScrC = detail_tScrCheck.isChecked
                }
                DB.getDao().updateData(dataModel)
            } else {
                dataModel.pm = detail_pmEditText.text.toString()
                val pm = detail_pmEditText.text.toString()
                val pmDay = Integer.valueOf("${pm[0]}${pm[1]}")
                val pmMonth = Integer.valueOf("${pm[2]}${pm[3]}") - 1
                val pmYear = Integer.valueOf("20${pm[4]}${pm[5]}")
                val cal = Calendar.getInstance()
                cal.set(Calendar.YEAR, pmYear)
                cal.set(Calendar.MONTH, pmMonth)
                cal.set(Calendar.DAY_OF_MONTH, pmDay)
                cal.add(Calendar.DAY_OF_YEAR, 77)
                dataModel.fScrS = cal.timeInMillis
                cal.add(Calendar.DAY_OF_YEAR, 20)
                dataModel.fScrE = cal.timeInMillis
                cal.add(Calendar.DAY_OF_YEAR, 29)
                dataModel.sScrS = cal.timeInMillis
                cal.add(Calendar.DAY_OF_YEAR, 20)
                dataModel.sScrE = cal.timeInMillis
                cal.add(Calendar.DAY_OF_YEAR, 64)
                dataModel.tScrS = cal.timeInMillis
                dataModel.thirtyWeeks = dataModel.tScrS
                cal.add(Calendar.DAY_OF_YEAR, 28)
                dataModel.tScrE = cal.timeInMillis
                cal.add(Calendar.DAY_OF_YEAR, 42)
                dataModel.fortyWeeks = cal.timeInMillis
                DB.getDao().addData(dataModel)
            }
            Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show()
            lockEditTexts()
        } else {
            Toast.makeText(context, getString(R.string.enterPM), Toast.LENGTH_LONG).show()
        }
    }

    private fun lockEditTexts() {
        detail_fioEditText.isEnabled = false
        detail_birthdayEditText.isEnabled = false
        detail_phoneEditText.isEnabled = false
        detail_releaseDateEditText.isEnabled = false
        detail_babyGenderSpinner.isEnabled = false
        detail_babyWeightEditText.isEnabled = false
        detail_babyHeightEditText.isEnabled = false
        detail_commentEditText.isEnabled = false
        detail_pmEditText.isEnabled = false
        detail_13weeksLinear.visibility = View.GONE
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

    private fun corrData() {
        dataModel.corr = true
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
        dataModel.fScrDate = tw
        val editWeeks = Integer.valueOf(detail_corrEditTextWeeks.text.toString())
        dataModel.fScrTimeWeeks = editWeeks.toString()
        val editDays = Integer.valueOf(detail_corrEditTextDays.text.toString())
        dataModel.fScrTimeDays = editDays.toString()
        val edit = 126 - (editWeeks * 7 + editDays)
        cal.add(Calendar.DAY_OF_YEAR, edit)
        dataModel.sScrSC = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 20)
        dataModel.sScrEC = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 64)
        dataModel.tScrSC = cal.timeInMillis
        dataModel.thirtyWeeksC = dataModel.tScrSC
        cal.add(Calendar.DAY_OF_YEAR, 28)
        dataModel.tScrEC = cal.timeInMillis
        cal.add(Calendar.DAY_OF_YEAR, 42)
        dataModel.fortyWeeksC = cal.timeInMillis
        DB.getDao().updateData(dataModel)
        Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show()
        lockEditTexts()
    }

    private fun todayTime(dataModel: DataModel) {
        val calNow = Calendar.getInstance()
        val calComp = Calendar.getInstance()
        val comp = if (dataModel.corr) {
            dataModel.fScrDate
        } else {
            dataModel.pm
        }
        calComp.set(Calendar.YEAR, Integer.valueOf("20${comp[4]}${comp[5]}"))
        calComp.set(Calendar.MONTH, Integer.valueOf("${comp[2]}${comp[3]}") - 1)
        calComp.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${comp[0]}${comp[1]}"))
        val diff = ((calNow.timeInMillis - calComp.timeInMillis) / (24 * 60 * 60 * 1000)).toInt()
        var diffWeeks = diff / 7
        var diffDays = diff % 7
        if (dataModel.corr) {
            diffWeeks += dataModel.fScrTimeWeeks.toInt()
            diffDays += dataModel.fScrTimeDays.toInt()
        }
        if (diffDays > 6) {
            diffWeeks++
            diffDays -= 7
        }
        detail_todayTimeWeeksTextView.text = diffWeeks.toString()
        detail_todayTimeDaysTextView.text = diffDays.toString()
        when {
            diffWeeks < 20 -> detail_todayTime.setBackgroundColor(Color.LTGRAY)
            diffWeeks < 30 -> detail_todayTime.setBackgroundColor(Color.GRAY)
            diffWeeks < 40 -> detail_todayTime.setBackgroundColor(Color.GREEN)
            else -> detail_todayTime.setBackgroundColor(Color.RED)
        }
    }

    private fun hideKeyboard(v: View) =
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    private fun calculateAge(dateOfBirth: String): String {
        return if (dateOfBirth.length == 6) {
            val calNow = Calendar.getInstance()
            val calBirth = Calendar.getInstance()
            if ("${dateOfBirth[4]}${dateOfBirth[5]}".toInt() < 50) {
                calBirth.set(Calendar.YEAR, Integer.valueOf("20${dateOfBirth[4]}${dateOfBirth[5]}"))
            } else {
                calBirth.set(Calendar.YEAR, Integer.valueOf("19${dateOfBirth[4]}${dateOfBirth[5]}"))
            }
            calBirth.set(Calendar.MONTH, Integer.valueOf("${dateOfBirth[2]}${dateOfBirth[3]}") - 1)
            calBirth.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${dateOfBirth[0]}${dateOfBirth[1]}"))
            val diff = ((calNow.timeInMillis - calBirth.timeInMillis) / 31536000000)
            "~$diff"
        } else {
            getString(R.string.incorrectBirthdayDate)
        }
    }
}
