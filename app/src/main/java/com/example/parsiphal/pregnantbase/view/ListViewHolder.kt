package com.example.parsiphal.pregnantbase.view

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_tab.view.*

class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val name = view.list_tab_name!!
    val birthday = view.list_tab_birthday!!

}