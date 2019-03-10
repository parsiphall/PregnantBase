package com.example.parsiphal.pregnantbase.recyclerView

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_tab.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val number = view.list_number!!
    val name = view.list_tab_name!!
    val screening = view.list_tab_scr!!
    val risk = view.list_tab_risk!!
    val multiplicity = view.list_tab_multiplicity!!
    val delete = view.delete_button!!
    val bg = view.list_tab_root!!
}