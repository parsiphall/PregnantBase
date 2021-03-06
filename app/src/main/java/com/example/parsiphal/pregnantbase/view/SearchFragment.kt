package com.example.parsiphal.pregnantbase.view

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.recyclerView.OnItemClickListener
import com.example.parsiphal.pregnantbase.recyclerView.SearchViewAdapter
import com.example.parsiphal.pregnantbase.recyclerView.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@ExperimentalCoroutinesApi
class SearchFragment : MvpAppCompatFragment() {

    private var items: List<DataModel> = ArrayList()
    lateinit var callBackActivity: MainView
    private lateinit var adapter: SearchViewAdapter
    var size = 0
    private var itemCounter = 1

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        menu.findItem(R.id.menu_detail_pdf).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_detail_pdf -> {
                GlobalScope.launch {
                    generatePDF(size)
                }
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        adapter = SearchViewAdapter(items, context)
        root.search_recyclerView.layoutManager = LinearLayoutManager(context)
        root.search_recyclerView.adapter = adapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        search_chooser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        search_editText.visibility = View.VISIBLE
                        search_dates.visibility = View.GONE
                        search_switch.visibility = View.GONE
                        search_editText.inputType = InputType.TYPE_CLASS_TEXT
                        search_editText.hint = getString(R.string.search_button)
                    }
                    1 -> {
                        search_editText.visibility = View.GONE
                        search_dates.visibility = View.VISIBLE
                        search_switch.visibility = View.VISIBLE
                        hideKeyboard(search_buttonGlobal)
                    }
                    5 -> {
                        search_editText.visibility = View.VISIBLE
                        search_dates.visibility = View.GONE
                        search_switch.visibility = View.GONE
                        search_editText.inputType = InputType.TYPE_CLASS_NUMBER
                        search_editText.hint = getString(R.string.inputFormatWeeks)
                    }
                    else -> {
                        search_editText.visibility = View.GONE
                        search_dates.visibility = View.VISIBLE
                        search_switch.visibility = View.GONE
                        hideKeyboard(search_buttonGlobal)
                    }
                }
            }
        }

        search_switch.setOnClickListener {
            search_switch.text = if (search_switch.isChecked) {
                getString(R.string.search_switch_act)
            } else {
                getString(R.string.search_switch_Nact)
            }
            GlobalScope.launch {
                searchScr(sdf)
            }
        }

        search_buttonGlobal.setOnClickListener {
            animate(it)
            when (search_chooser.selectedItemPosition) {
                0 -> {
                    GlobalScope.launch {
                        searchFio()
                    }
                }
                1 -> {
                    GlobalScope.launch {
                        searchScr(sdf)
                    }
                }
                2 -> {
                    GlobalScope.launch {
                        searchFScr(sdf)
                    }
                }
                3 -> {
                    GlobalScope.launch {
                        searchSScr(sdf)
                    }
                }
                4 -> {
                    GlobalScope.launch {
                        searchTScr(sdf)
                    }
                }
                5 -> {
                    GlobalScope.launch {
                        searchWeeks()
                    }
                }
            }
            itemCounter = 1
            hideKeyboard(it)
        }
        search_buttonReset.setOnClickListener {
            animate(it)
            when (search_chooser.selectedItemPosition) {
                0, 5 -> {
                    search_editText.setText("")
                }
                else -> {
                    search_textView1.text = ""
                    search_textView2.text = ""
                }
            }
        }
        search_textView1.setOnClickListener {
            datePickerDialog(it as TextView)
        }
        search_textView2.setOnClickListener {
            datePickerDialog(it as TextView)
        }
        search_recyclerView.addOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val bundle = Bundle()
                bundle.putSerializable("ITEM", items[position])
                callBackActivity.fragmentPlaceWithArgs(DetailsFragment(), bundle)
            }
        })
    }

    private fun dateListener(v: TextView): DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            var myMonth = (month + 1).toString()
            var myDay = dayOfMonth.toString()
            if (month < 9) {
                myMonth = "0$myMonth"
            }
            if (dayOfMonth < 10) {
                myDay = "0$myDay"
            }
            val date = "$myDay/$myMonth/$year"
            v.text = date
        }

    private fun datePickerDialog(v: TextView) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            context!!,
            dateListener(v),
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private fun animate(it: View?) {
        YoYo.with(Techniques.Landing)
            .duration(100)
            .playOn(it)
    }

    private suspend fun searchWeeks() {
        val cal = Calendar.getInstance().timeInMillis
        val search = if (search_editText.text.toString().isNotEmpty()) {
            search_editText.text.toString().toInt()
        } else {
            0
        }
        items = if (prefs.district == 0) {
            DB.getDao().getWeeksAll(search, cal)
        } else {
            DB.getDao().getWeeksDistr(search, cal, prefs.district)
        }
        size = items.size
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchFio() {
        val search = "%${search_editText.text}%"
        items = if (prefs.district == 0) {
            DB.getDao().getCurrentDataAll(search)
        } else {
            DB.getDao().getCurrentDataDistr(search, prefs.district)
        }
        size = items.size
        Collections.sort(items) { object1, object2 -> object1.name.compareTo(object2.name) }
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchTScr(sdf: SimpleDateFormat) {
        val field1 = search_textView1.text.toString()
        val field2 = search_textView2.text.toString()
        items = when {
            field1.length == 10 && field2.length == 10 -> {
                val date1 = sdf.parse(field1)
                val date2 = sdf.parse(field2)
                if (prefs.district == 0) {
                    DB.getDao().getTScrRangeAll(date1.time, date2.time)
                } else {
                    DB.getDao().getTScrRangeDistr(
                        date1.time, date2.time,
                        prefs.district
                    )
                }
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                if (prefs.district == 0) {
                    DB.getDao().getTScrAll(date.time)
                } else {
                    DB.getDao().getTScrDistr(date.time, prefs.district)
                }
            }
            else -> {
                if (prefs.district == 0) {
                    DB.getDao().getTScrAllAll()
                } else {
                    DB.getDao().getTScrAllDistr(prefs.district)
                }
            }
        }
        size = items.size
        Collections.sort(items) { object1, object2 ->
            val x1 = if (object1.corr) {
                object1.tScrSC
            } else {
                object1.tScrS
            }
            val x2 = if (object2.corr) {
                object2.tScrSC
            } else {
                object2.tScrS
            }
            x1.compareTo(x2)
        }
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchSScr(sdf: SimpleDateFormat) {
        val field1 = search_textView1.text.toString()
        val field2 = search_textView2.text.toString()
        items = when {
            field1.length == 10 && field2.length == 10 -> {
                val date1 = sdf.parse(field1)
                val date2 = sdf.parse(field2)
                if (prefs.district == 0) {
                    DB.getDao().getSScrRangeAll(date1.time, date2.time)
                } else {
                    DB.getDao().getSScrRangeDistr(
                        date1.time, date2.time,
                        prefs.district
                    )
                }
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                if (prefs.district == 0) {
                    DB.getDao().getSScrAll(date.time)
                } else {
                    DB.getDao().getSScrDistr(date.time, prefs.district)
                }
            }
            else -> {
                if (prefs.district == 0) {
                    DB.getDao().getSScrAllAll()
                } else {
                    DB.getDao().getSScrAllDistr(prefs.district)
                }
            }
        }
        size = items.size
        Collections.sort(items) { object1, object2 ->
            val x1 = if (object1.corr) {
                object1.sScrSC
            } else {
                object1.sScrS
            }
            val x2 = if (object2.corr) {
                object2.sScrSC
            } else {
                object2.sScrS
            }

            x1.compareTo(x2)
        }
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchFScr(sdf: SimpleDateFormat) {
        val field1 = search_textView1.text.toString()
        val field2 = search_textView2.text.toString()
        items = when {
            field1.length == 10 && field2.length == 10 -> {
                val date1 = sdf.parse(field1)
                val date2 = sdf.parse(field2)
                if (prefs.district == 0) {
                    DB.getDao().getFScrRangeAll(date1.time, date2.time)
                } else {
                    DB.getDao().getFScrRangeDistr(
                        date1.time, date2.time,
                        prefs.district
                    )
                }
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                if (prefs.district == 0) {
                    DB.getDao().getFScrAll(date.time)
                } else {
                    DB.getDao().getFScrDistr(date.time, prefs.district)
                }
            }
            else -> {
                if (prefs.district == 0) {
                    DB.getDao().getFScrAllAll()
                } else {
                    DB.getDao().getFScrAllDistr(prefs.district)
                }
            }
        }
        size = items.size
        Collections.sort(items) { object1, object2 ->
            object1.fScrS.compareTo(object2.fScrS)
        }
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchScr(sdf: SimpleDateFormat) {
        val field1 = search_textView1.text.toString()
        val field2 = search_textView2.text.toString()
        items = when {
            field1.length == 10 && field2.length == 10 -> {
                val date1 = sdf.parse(field1)
                val date2 = sdf.parse(field2)
                if (prefs.district == 0) {
                    DB.getDao().getScrRangeAll(date1.time, date2.time)
                } else {
                    DB.getDao().getScrRangeDistr(
                        date1.time, date2.time,
                        prefs.district
                    )
                }
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                if (prefs.district == 0) {
                    DB.getDao().getScrAll(date.time)
                } else {
                    DB.getDao().getScrDistr(date.time, prefs.district)
                }
            }
            else -> {
                if (prefs.district == 0) {
                    DB.getDao().getScrAllAll()
                } else {
                    DB.getDao().getScrAllDistr(prefs.district)
                }
            }
        }
        size = items.size
        if (search_switch.isChecked) {
            Collections.sort(items) { object1, object2 ->
                var x1 = object1.fScrS
                if (object1.fScrC) {
                    x1 = object1.sScrS
                    if (object1.corr) {
                        x1 = object1.sScrSC
                    }
                }
                if (object1.sScrC) {
                    x1 = object1.tScrS
                    if (object1.corr) {
                        x1 = object1.tScrSC
                    }
                }
                var x2 = object2.fScrS
                if (object2.fScrC) {
                    x2 = object2.sScrS
                    if (object2.corr) {
                        x2 = object2.sScrSC
                    }
                }
                if (object2.sScrC) {
                    x2 = object2.tScrS
                    if (object2.corr) {
                        x2 = object2.tScrSC
                    }
                }
                x1.compareTo(x2)
            }
        } else {
            Collections.sort(items) { object1, object2 ->
                var x1 = object1.fScrE
                if (object1.fScrC) {
                    x1 = object1.sScrE
                    if (object1.corr) {
                        x1 = object1.sScrEC
                    }
                }
                if (object1.sScrC) {
                    x1 = object1.tScrE
                    if (object1.corr) {
                        x1 = object1.tScrEC
                    }
                }
                var x2 = object2.fScrE
                if (object2.fScrC) {
                    x2 = object2.sScrE
                    if (object2.corr) {
                        x2 = object2.sScrEC
                    }
                }
                if (object2.sScrC) {
                    x2 = object2.tScrE
                    if (object2.corr) {
                        x2 = object2.tScrEC
                    }
                }
                x1.compareTo(x2)
            }
        }
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private fun hideKeyboard(v: View) =
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    private suspend fun generatePDF(size: Int) {
        var position = (itemCounter + 1) * 12 - 1
        if (size / 12 > itemCounter) {
            itemCounter++
        } else {
            itemCounter++
            position = size - 1
        }
        MainScope().launch {
            search_recyclerView.layoutManager?.scrollToPosition(position)
        }
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = document.startPage(pageInfo)
        search_relative_root.draw(page.canvas)
        document.finishPage(page)
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/PregnantBase")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val targetPDF = "${dir.absolutePath}/${search_chooser.selectedItem}_${itemCounter - 1}.pdf"
        val filePath = File(targetPDF)
        try {
            val outputStream = FileOutputStream(filePath)
            document.writeTo(outputStream)
            MainScope().launch {
                Toast.makeText(context, "Файл сохранён", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            MainScope().launch {
                Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
            }
        } finally {
            document.close()
        }
    }
}
