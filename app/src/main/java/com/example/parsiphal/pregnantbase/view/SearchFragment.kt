package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.inteface.OnItemClickListener
import com.example.parsiphal.pregnantbase.inteface.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import java.text.SimpleDateFormat

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
            Toast.makeText(activity?.applicationContext, "${items.size}", Toast.LENGTH_LONG).show()
            adapter.dataChanged(items)
        }
        button_search_Scr.setOnClickListener {
            val date = sdf.parse(search_editText.text.toString())
            items = DB.getDao().getScr(date.time)
            adapter.dataChanged(items)
        }
        button_search_fScr.setOnClickListener {
            val date = sdf.parse(search_editText.text.toString())
            items = DB.getDao().getFScr(date.time)
            adapter.dataChanged(items)
        }
        button_search_sScr.setOnClickListener {
            val date = sdf.parse(search_editText.text.toString())
            items = DB.getDao().getSScr(date.time)
            adapter.dataChanged(items)
        }
        button_search_tScr.setOnClickListener {
            val date = sdf.parse(search_editText.text.toString())
            items = DB.getDao().getTScr(date.time)
            adapter.dataChanged(items)
        }
        search_recyclerView.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val bundle = Bundle()
                bundle.putSerializable("ITEM", items[position])
                callBackActivity.fragmentPlaceWithArgs(DetailsFragment(), bundle)
            }
        })
    }
}
