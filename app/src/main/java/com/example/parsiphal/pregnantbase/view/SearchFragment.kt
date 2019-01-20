package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.inteface.OnItemClickListener
import com.example.parsiphal.pregnantbase.inteface.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : MvpAppCompatFragment() {

    private var items: List<DataModel> = ArrayList()
    lateinit var callBackActivity: MainView
    lateinit var adapter: ListViewAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBackActivity = context as MainView
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
            if (field.length == 6) {
                val date = sdf.parse(field)
                items = DB.getDao().getScr(date.time)
            } else if (field.length == 4) {
                val week = Integer.valueOf("${field[0]}${field[1]}")
                val year = Integer.valueOf("20${field[2]}${field[3]}")
                items = DB.getDao().getScrWeek(startOfWeek(week, year), endOfWeek(week, year))
            }
            Collections.sort(items) { object1, object2 ->
                var x1 = object1.fScrE
                if (object1.fScrC) {
                    x1 = object1.sScrE
                }
                if (object1.sScrC) {
                    x1 = object1.tScrE
                }
                var x2 = object2.fScrE
                if (object2.fScrC) {
                    x2 = object2.sScrE
                }
                if (object1.sScrC) {
                    x2 = object2.tScrE
                }
                x1.compareTo(x2) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_fScr.setOnClickListener {
            val field = search_editText.text.toString()
            if (field.length == 6) {
                val date = sdf.parse(field)
                items = DB.getDao().getFScr(date.time)
            } else if (field.length == 4) {
                val week = Integer.valueOf("${field[0]}${field[1]}")
                val year = Integer.valueOf("20${field[2]}${field[3]}")
                items = DB.getDao().getFScrWeek(startOfWeek(week, year), endOfWeek(week, year))
            }
            Collections.sort(items) { object1, object2 -> object1.fScrE.compareTo(object2.fScrE) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_sScr.setOnClickListener {
            val field = search_editText.text.toString()
            if (field.length == 6) {
                val date = sdf.parse(field)
                items = DB.getDao().getSScr(date.time)
            } else if (field.length == 4) {
                val week = Integer.valueOf("${field[0]}${field[1]}")
                val year = Integer.valueOf("20${field[2]}${field[3]}")
                items = DB.getDao().getSScrWeek(startOfWeek(week, year), endOfWeek(week, year))
            }
            Collections.sort(items) { object1, object2 -> object1.sScrE.compareTo(object2.sScrE) }
            adapter.dataChanged(items)
            list_tab_count.text = items.size.toString()
            hideKeyboard(it)
        }
        button_search_tScr.setOnClickListener {
            val field = search_editText.text.toString()
            if (field.length == 6) {
                val date = sdf.parse(field)
                items = DB.getDao().getTScr(date.time)
            } else if (field.length == 4) {
                val week = Integer.valueOf("${field[0]}${field[1]}")
                val year = Integer.valueOf("20${field[2]}${field[3]}")
                items = DB.getDao().getTScrWeek(startOfWeek(week, year), endOfWeek(week, year))
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
}
