package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel

class ListViewAdapter(private var items: List<DataModel>,
                      private val context: Context?): RecyclerView.Adapter<ListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.name.text = items[position].name
        holder.birthday.text = items[position].birthday
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.list_tab, parent, false))
    }

    fun dataChanged(newItems: List<DataModel>){
        items = newItems
        notifyDataSetChanged()
    }
}