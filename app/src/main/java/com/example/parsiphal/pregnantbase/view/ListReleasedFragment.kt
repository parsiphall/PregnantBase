package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.inteface.OnItemClickListener
import com.example.parsiphal.pregnantbase.inteface.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.list_tab.*
import java.util.Collections.sort

class ListReleasedFragment : MvpAppCompatFragment() {

    private var items: List<DataModel> = ArrayList()
    private lateinit var callBackActivity: MainView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callBackActivity = context as MainView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        getDataList()
        root.list_recycler_view.layoutManager = LinearLayoutManager(context)
        root.list_recycler_view.adapter = ListViewAdapter(items, context)
        return root
    }

    private fun getDataList(): List<DataModel> {
        items = DB.getDao().getDataReleased()
        sort(items) { object1, object2 -> object1.name.compareTo(object2.name) }
        return items
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_tab_count.text = items.size.toString()
        list_tab_released_screening.visibility = View.INVISIBLE
        list_recycler_view.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val bundle = Bundle()
                bundle.putSerializable("ITEM", items[position])
                callBackActivity.fragmentPlaceWithArgs(DetailsFragment(), bundle)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        list_recycler_view.adapter!!.notifyDataSetChanged()
    }
}