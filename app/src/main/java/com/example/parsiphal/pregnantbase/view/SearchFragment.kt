package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.inteface.OnItemClickListener
import com.example.parsiphal.pregnantbase.inteface.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : MvpAppCompatFragment() {

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        menu.findItem(R.id.menu_detail_pdf).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_detail_pdf -> {
                generatePDF()
                return true
            }
        }
        return false
    }

    private var items: List<DataModel> = ArrayList()
    lateinit var callBackActivity: MainView
    lateinit var adapter: ListViewAdapter

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
        adapter = ListViewAdapter(items, context)
        root.search_recyclerView.layoutManager = LinearLayoutManager(context)
        root.search_recyclerView.adapter = adapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("ddMMyy")
        button_search_fio.setOnClickListener {
            val search = "%${search_editText.text}%"
            items = DB.getDao().getCurrentData(search)
            Collections.sort(items) { object1, object2 -> object1.name.compareTo(object2.name) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_Scr.setOnClickListener {
            val field = search_editText.text.toString()
            items = when {
                field.length == 6 -> {
                    val date = sdf.parse(field)
                    DB.getDao().getScr(date.time)
                }
                field.length == 4 -> {
                    val week = Integer.valueOf("${field[0]}${field[1]}")
                    val year = Integer.valueOf("20${field[2]}${field[3]}")
                    DB.getDao().getScrWeek(startOfWeek(week, year), endOfWeek(week, year))
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
                if (object1.sScrC) {
                    x2 = object2.tScrE
                    if (object2.corr) {
                        x2 = object2.tScrEC
                    }
                }
                x1.compareTo(x2)
            }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_fScr.setOnClickListener {
            val field = search_editText.text.toString()
            items = when {
                field.length == 6 -> {
                    val date = sdf.parse(field)
                    DB.getDao().getFScr(date.time)
                }
                field.length == 4 -> {
                    val week = Integer.valueOf("${field[0]}${field[1]}")
                    val year = Integer.valueOf("20${field[2]}${field[3]}")
                    DB.getDao().getFScrWeek(startOfWeek(week, year), endOfWeek(week, year))
                }
                else -> DB.getDao().getFScrAll()
            }
            Collections.sort(items) { object1, object2 -> object1.fScrE.compareTo(object2.fScrE) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_sScr.setOnClickListener {
            val field = search_editText.text.toString()
            items = when {
                field.length == 6 -> {
                    val date = sdf.parse(field)
                    DB.getDao().getSScr(date.time)
                }
                field.length == 4 -> {
                    val week = Integer.valueOf("${field[0]}${field[1]}")
                    val year = Integer.valueOf("20${field[2]}${field[3]}")
                    DB.getDao().getSScrWeek(startOfWeek(week, year), endOfWeek(week, year))
                }
                else -> DB.getDao().getSScrAll()
            }
            Collections.sort(items) { object1, object2 -> object1.sScrE.compareTo(object2.sScrE) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_tScr.setOnClickListener {
            val field = search_editText.text.toString()
            items = when {
                field.length == 6 -> {
                    val date = sdf.parse(field)
                    DB.getDao().getTScr(date.time)
                }
                field.length == 4 -> {
                    val week = Integer.valueOf("${field[0]}${field[1]}")
                    val year = Integer.valueOf("20${field[2]}${field[3]}")
                    DB.getDao().getTScrWeek(startOfWeek(week, year), endOfWeek(week, year))
                }
                else -> DB.getDao().getTScrAll()
            }
            Collections.sort(items) { object1, object2 -> object1.tScrE.compareTo(object2.tScrE) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        search_recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val bundle = Bundle()
                bundle.putSerializable("ITEM", items[position])
                callBackActivity.fragmentPlaceWithArgs(DetailsFragment(), bundle)
            }
        })
    }

    private fun startOfWeek(week: Int, year: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.WEEK_OF_YEAR, week)
        cal.get(Calendar.WEEK_OF_YEAR)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun endOfWeek(week: Int, year: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.WEEK_OF_YEAR, week)
        cal.get(Calendar.WEEK_OF_YEAR)
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun hideKeyboard(v: View) =
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            v.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

    private fun generatePDF() {
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
