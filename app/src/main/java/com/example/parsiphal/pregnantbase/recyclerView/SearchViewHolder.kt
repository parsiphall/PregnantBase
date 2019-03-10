package com.example.parsiphal.pregnantbase.recyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.search_tab.view.*

class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val number = view.search_number!!
    val name = view.search_tab_name!!
    val screening = view.search_tab_scr!!
    val risk = view.search_tab_risk!!
    val multiplicity = view.search_tab_multiplicity!!
    val bg = view.search_tab_root!!

}