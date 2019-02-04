package com.example.parsiphal.pregnantbase.view

import android.app.DatePickerDialog
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

const val DIALOG_DATE = 1

class DetailsFragment : MvpAppCompatFragment() {

    private var newData = false
    private lateinit var dataModel: DataModel
    private lateinit var callBackActivity: MainView
    private var birthdayDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        var myMonth = (month + 1).toString()
        var myDay = dayOfMonth.toString()
        if (month < 10) {
            myMonth = "0$myMonth"
        }
        if (dayOfMonth < 10) {
            myDay = "0$myDay"
        }
        val date = "$myDay/$myMonth/$year"
        detail_birthdayEdit.text = date
    }
    private var releaseDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        var myMonth = (month + 1).toString()
        var myDay = dayOfMonth.toString()
        if (month < 10) {
            myMonth = "0$myMonth"
        }
        if (dayOfMonth < 10) {
            myDay = "0$myDay"
        }
        val date = "$myDay/$myMonth/$year"
        detail_releaseDateEdit.text = date
    }
    private var corrDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        var myMonth = (month + 1).toString()
        var myDay = dayOfMonth.toString()
        if (month < 10) {
            myMonth = "0$myMonth"
        }
        if (dayOfMonth < 10) {
            myDay = "0$myDay"
        }
        val date = "$myDay/$myMonth/$year"
        detail_corrEdit.text = date
    }
    private var pmDatePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        var myMonth = (month + 1).toString()
        var myDay = dayOfMonth.toString()
        if (month < 10) {
            myMonth = "0$myMonth"
        }
        if (dayOfMonth < 10) {
            myDay = "0$myDay"
        }
        val date = "$myDay/$myMonth/$year"
        detail_pmEdit.text = date
    }

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
                GlobalScope.launch {
                    generatePDF(dataModel.name)
                }
                return true
            }
            R.id.menu_detail_save -> {
                GlobalScope.launch {
                    saveToBase()
                }
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
                return true
            }
        }
        return false
    }

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        if (!newData) {
            detail_fioEditText.isEnabled = false
            detail_birthdayEdit.isEnabled = false
            detail_phoneEditText.isEnabled = false
            detail_pmEdit.isEnabled = false
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
                val fScrDay: Int
                val fScrMonth: Int
                val fScrYear: Int
                if (dataModel.fScrDate.length == 10) {
                    fScrDay = Integer.valueOf("${fScrDate[0]}${fScrDate[1]}")
                    fScrMonth = Integer.valueOf("${fScrDate[3]}${fScrDate[4]}") - 1
                    fScrYear = Integer.valueOf("${fScrDate[6]}${fScrDate[7]}${fScrDate[8]}${fScrDate[9]}")
                } else {
                    fScrDay = Integer.valueOf("${fScrDate[0]}${fScrDate[1]}")
                    fScrMonth = Integer.valueOf("${fScrDate[2]}${fScrDate[3]}") - 1
                    fScrYear = Integer.valueOf("20${fScrDate[4]}${fScrDate[5]}")
                }
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
                detail_releaseDateEdit.isEnabled = false
                detail_babyWeightEditText.isEnabled = false
                detail_babyHeightEditText.isEnabled = false

                if (dataModel.releaseDate.length == 6) {
                    var rd = dataModel.releaseDate
                    rd = "${rd[0]}${rd[1]}/${rd[2]}${rd[3]}/${rd[4]}${rd[5]}"
                    detail_releaseDateEdit.text = rd
                } else {
                    detail_releaseDateEdit.text = dataModel.releaseDate
                }
                detail_babyGenderSpinner.setSelection(dataModel.babyGender)
                detail_babyWeightEditText.setText(dataModel.babyWeight)
                detail_babyHeightEditText.setText(dataModel.babyHeight)
            }
            todayTime(dataModel)
        }
        detail_fioEditText.setText(dataModel.name)
        if (dataModel.birthday.length == 6) {
            var bd = dataModel.birthday
            bd = "${bd[0]}${bd[1]}/${bd[2]}${bd[3]}/${bd[4]}${bd[5]}"
            detail_birthdayEdit.text = bd
        } else {
            detail_birthdayEdit.text = dataModel.birthday
        }
        if (dataModel.phone.length == 11) {
            var p = dataModel.phone
            p = "+7(${p[1]}${p[2]}${p[3]})${p[4]}${p[5]}${p[6]}-${p[7]}${p[8]}-${p[9]}${p[10]}"
            detail_phoneEditText.setText(p)
        } else {
            detail_phoneEditText.setText(dataModel.phone)
        }
        if (dataModel.pm.length == 6) {
            var pm = dataModel.pm
            pm = "${pm[0]}${pm[1]}/${pm[2]}${pm[3]}/${pm[4]}${pm[5]}"
            detail_pmEdit.text = pm
        } else {
            detail_pmEdit.text = dataModel.pm
        }
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

        detail_birthdayEdit.setOnClickListener {
            DatePickerDialog(
                context!!,
                birthdayDatePicker,
                1990,
                0,
                1
            ).show()
        }

        detail_releaseDateEdit.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                context!!,
                releaseDatePicker,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        detail_corrEdit.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                context!!,
                corrDatePicker,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        detail_pmEdit.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                context!!,
                pmDatePicker,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun correctData() {
        detail_fioEditText.isEnabled = true
        detail_birthdayEdit.isEnabled = true
        detail_phoneEditText.isEnabled = true
        detail_releaseDateEdit.isEnabled = true
        detail_babyGenderSpinner.isEnabled = true
        detail_babyWeightEditText.isEnabled = true
        detail_babyHeightEditText.isEnabled = true
        detail_commentEditText.isEnabled = true
        if (!dataModel.corr) {
            detail_13weeksLinear.visibility = View.VISIBLE
            detail_corrSaveButton.setOnClickListener {
                hideKeyboard(it)
                GlobalScope.launch {
                    corrData()
                }
            }
        }
    }

    private suspend fun generatePDF(name: String) {
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
            MainScope().launch {
                Toast.makeText(context, "Файл $name.pdf сохранён", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            MainScope().launch {
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show()
            }
        }
        document.close()
    }

    private suspend fun saveToBase() {
        dataModel.name = detail_fioEditText.text.toString()
        dataModel.birthday = detail_birthdayEdit.text.toString()
        dataModel.phone = detail_phoneEditText.text.toString()
        dataModel.release = detail_releaseCheckBox.isChecked
        dataModel.multiplicity = detail_multiplicityCheckBox.isChecked
        dataModel.risk = detail_riskSpinner.selectedItemPosition
        dataModel.riskText = detail_riskSpinner.selectedItem.toString()
        dataModel.releaseDate = detail_releaseDateEdit.text.toString()
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
            if (detail_pmEdit.text.toString().length == 10) {
                dataModel.pm = detail_pmEdit.text.toString()
                val pm = detail_pmEdit.text.toString()
                val pmDay = Integer.valueOf("${pm[0]}${pm[1]}")
                val pmMonth = Integer.valueOf("${pm[3]}${pm[4]}") - 1
                val pmYear = Integer.valueOf("${pm[6]}${pm[7]}${pm[8]}${pm[9]}")
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
            } else {
                MainScope().launch {
                    Toast.makeText(context, getString(R.string.enterPM), Toast.LENGTH_LONG).show()
                }
            }
        }
        MainScope().launch {
            Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show()
            lockEditTexts()
        }

    }

    private fun lockEditTexts() {
        detail_fioEditText.isEnabled = false
        detail_birthdayEdit.isEnabled = false
        detail_phoneEditText.isEnabled = false
        detail_releaseDateEdit.isEnabled = false
        detail_babyWeightEditText.isEnabled = false
        detail_babyHeightEditText.isEnabled = false
        detail_commentEditText.isEnabled = false
        detail_pmEdit.isEnabled = false
        detail_13weeksLinear.visibility = View.GONE
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }

    private suspend fun corrData() {
        dataModel.corr = true
        dataModel.fScrC = detail_fScrCheck.isChecked
        dataModel.sScrC = detail_sScrCheck.isChecked
        dataModel.tScrC = detail_tScrCheck.isChecked
        dataModel.fScrC = true
        val tw = detail_corrEdit.text.toString()
        val twDay = Integer.valueOf("${tw[0]}${tw[1]}")
        val twMonth = Integer.valueOf("${tw[3]}${tw[4]}") - 1
        val twYear = Integer.valueOf("${tw[6]}${tw[7]}${tw[8]}${tw[9]}")
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
        MainScope().launch {
            Toast.makeText(context, getString(R.string.saved), Toast.LENGTH_LONG).show()
            lockEditTexts()
        }
    }

    private fun todayTime(dataModel: DataModel) {
        val calNow = Calendar.getInstance()
        val calComp = Calendar.getInstance()
        val comp = if (dataModel.corr) {
            dataModel.fScrDate
        } else {
            dataModel.pm
        }
        if (comp.length == 6) {
            calComp.set(Calendar.YEAR, Integer.valueOf("20${comp[4]}${comp[5]}"))
            calComp.set(Calendar.MONTH, Integer.valueOf("${comp[2]}${comp[3]}") - 1)
        } else {
            calComp.set(Calendar.YEAR, Integer.valueOf("${comp[6]}${comp[7]}${comp[8]}${comp[9]}"))
            calComp.set(Calendar.MONTH, Integer.valueOf("${comp[3]}${comp[4]}") - 1)
        }
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
            diffWeeks < 20 -> detail_todayTime.setBackgroundColor(Color.CYAN)
            diffWeeks < 30 -> detail_todayTime.setBackgroundColor(Color.YELLOW)
            diffWeeks < 40 -> detail_todayTime.setBackgroundColor(Color.GREEN)
            else -> detail_todayTime.setBackgroundColor(Color.RED)
        }
    }

    private fun hideKeyboard(v: View) =
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    private fun calculateAge(dateOfBirth: String): String = when {
        dateOfBirth.length == 10 -> {
            val calNow = Calendar.getInstance()
            val calBirth = Calendar.getInstance()
            calBirth.set(
                Calendar.YEAR,
                Integer.valueOf("${dateOfBirth[6]}${dateOfBirth[7]}${dateOfBirth[8]}${dateOfBirth[9]}")
            )
            calBirth.set(Calendar.MONTH, Integer.valueOf("${dateOfBirth[3]}${dateOfBirth[4]}") - 1)
            calBirth.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${dateOfBirth[0]}${dateOfBirth[1]}"))
            val diff = ((calNow.timeInMillis - calBirth.timeInMillis) / 31536000000)
            "~$diff"
        }
        dateOfBirth.length == 8 -> {
            val calNow = Calendar.getInstance()
            val calBirth = Calendar.getInstance()
            if ("${dateOfBirth[6]}${dateOfBirth[7]}".toInt() < 50) {
                calBirth.set(Calendar.YEAR, Integer.valueOf("20${dateOfBirth[6]}${dateOfBirth[7]}"))
            } else {
                calBirth.set(Calendar.YEAR, Integer.valueOf("19${dateOfBirth[6]}${dateOfBirth[7]}"))
            }
            calBirth.set(Calendar.MONTH, Integer.valueOf("${dateOfBirth[3]}${dateOfBirth[4]}") - 1)
            calBirth.set(Calendar.DAY_OF_MONTH, Integer.valueOf("${dateOfBirth[0]}${dateOfBirth[1]}"))
            val diff = ((calNow.timeInMillis - calBirth.timeInMillis) / 31536000000)
            "~$diff"
        }
        dateOfBirth.length == 6 -> {
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
        }
        else -> getString(R.string.incorrectBirthdayDate)
    }

}
