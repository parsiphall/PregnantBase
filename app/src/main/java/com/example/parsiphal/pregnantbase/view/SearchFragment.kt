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
import com.example.parsiphal.pregnantbase.inteface.OnItemClickListener
import com.example.parsiphal.pregnantbase.inteface.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : MvpAppCompatFragment() {
    private var items: List<DataModel> = ArrayList()
    lateinit var callBackActivity: MainView
    private lateinit var adapter: SearchViewAdapter

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
//        menu.findItem(R.id.menu_detail_pdf).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_detail_pdf -> {
                GlobalScope.launch {
                    generatePDF()
                }
                search_recyclerView.layoutManager?.scrollToPosition(8)
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
                        search_editText.inputType = InputType.TYPE_CLASS_TEXT
                        search_editText.hint = getString(R.string.search_button)
                    }
                    5 -> {
                        search_editText.visibility = View.VISIBLE
                        search_dates.visibility = View.GONE
                        search_editText.inputType = InputType.TYPE_CLASS_NUMBER
                        search_editText.hint = getString(R.string.inputFormatWeeks)
                    }
                    else -> {
                        search_editText.visibility = View.GONE
                        search_dates.visibility = View.VISIBLE
                        hideKeyboard(search_buttonGlobal)
                    }
                }
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
        search_recyclerView.addOnItemClickListener(object : OnItemClickListener {
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
        val search = search_editText.text.toString().toInt()
        items = DB.getDao().getWeeks(search, cal)
        MainScope().launch {
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
        }
    }

    private suspend fun searchFio() {
        val search = "%${search_editText.text}%"
        items = DB.getDao().getCurrentData(search)
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
                DB.getDao().getTScrRange(date1.time, date2.time)
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                DB.getDao().getTScr(date.time)
            }
            else -> DB.getDao().getTScrAll()
        }
        Collections.sort(items) { object1, object2 ->

            val x1 = if (object1.corr) {
                object1.tScrEC
            } else {
                object1.tScrE
            }
            val x2 = if (object2.corr) {
                object2.tScrEC
            } else {
                object2.tScrE
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
                DB.getDao().getSScrRange(date1.time, date2.time)
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                DB.getDao().getSScr(date.time)
            }
            else -> DB.getDao().getSScrAll()
        }
        Collections.sort(items) { object1, object2 ->
            val x1 = if (object1.corr) {
                object1.sScrEC
            } else {
                object1.sScrE
            }
            val x2 = if (object2.corr) {
                object2.sScrEC
            } else {
                object2.sScrE
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
                DB.getDao().getFScrRange(date1.time, date2.time)
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                DB.getDao().getFScr(date.time)
            }
            else -> DB.getDao().getFScrAll()
        }
        Collections.sort(items) { object1, object2 ->
            object1.fScrE.compareTo(object2.fScrE)
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
                DB.getDao().getScrRange(date1.time, date2.time)
            }
            field1.length == 10 -> {
                val date = sdf.parse(field1)
                DB.getDao().getScr(date.time)
            }
            else -> DB.getDao().getScrAll()
        }
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

    private suspend fun generatePDF() {
//        val size = items.size
//        var itemCounter = 0
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, 1).create()
        val page = document.startPage(pageInfo)
        search_relative.draw(page.canvas)
        document.finishPage(page)
        val dir = File(Environment.getExternalStorageDirectory().absolutePath + "/PregnantBase")
        if (!dir.exists()) {
            dir.mkdir()
        }
        val targetPDF = "${dir.absolutePath}/list.pdf"
        val filePath = File(targetPDF)
        try {
            val outputStream = FileOutputStream(filePath)
            document.writeTo(outputStream)
            Toast.makeText(context, "Файл list.pdf сохранён", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Ошибка", Toast.LENGTH_LONG).show()
        }
        document.close()
    }
}
