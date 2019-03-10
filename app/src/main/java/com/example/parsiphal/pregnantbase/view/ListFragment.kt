package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment

import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import com.example.parsiphal.pregnantbase.inteface.MainView
import com.example.parsiphal.pregnantbase.recyclerView.ListViewAdapter
import com.example.parsiphal.pregnantbase.recyclerView.OnItemClickListener
import com.example.parsiphal.pregnantbase.recyclerView.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.coroutines.*
import java.util.*
import java.util.Collections.sort

class ListFragment : MvpAppCompatFragment() {


    private var items: List<DataModel> = ArrayList()
    private lateinit var callBackActivity: MainView
    private lateinit var adapter: ListViewAdapter

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        menu.findItem(R.id.menu_detail_add).isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_detail_add -> {
                callBackActivity.fragmentPlace(DetailsFragment())
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
        val root = inflater.inflate(R.layout.fragment_list, container, false)
        adapter = ListViewAdapter(items, context)
        root.list_recycler_view.layoutManager = LinearLayoutManager(context)
        root.list_recycler_view.adapter = adapter
        return root
    }

    private suspend fun getDataList() {
        items = DB.getDao().getDataPregnant()
        sort(items) { object1, object2 -> object1.name.compareTo(object2.name) }
        MainScope().launch {
            list_tab_count.text = items.size.toString()
            adapter.dataChanged(items)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            getDataList()
        }
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
