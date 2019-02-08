package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import java.text.SimpleDateFormat
import java.util.*

class SearchViewAdapter(
    private var items: List<DataModel>,
    private val context: Context?
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.search_tab, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val cal = Calendar.getInstance()
        val numberText = "${(position + 1)}/${items.size}"
        val risk = items[position].riskText[0]
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var screening = ""
        if (!items[position].fScrC) {
            screening =
                "${context!!.resources.getString(R.string.detail_fScr)} ${sdf.format(items[position].fScrS)} - ${sdf.format(
                    items[position].fScrE
                )}"
        } else if (!items[position].sScrC) {
            screening =
                "${context!!.resources.getString(R.string.detail_sScr)} ${sdf.format(items[position].sScrS)} - ${sdf.format(
                    items[position].sScrE
                )}"
            if (items[position].corr) {
                screening =
                    "${context.resources.getString(R.string.detail_sScr)} ${sdf.format(items[position].sScrSC)} - ${sdf.format(
                        items[position].sScrEC
                    )}"
            }
        } else if (!items[position].tScrC) {
            screening =
                "${context!!.resources.getString(R.string.detail_tScr)} ${sdf.format(items[position].tScrS)} - ${sdf.format(
                    items[position].tScrE
                )}"
            if (items[position].corr) {
                screening =
                    "${context.resources.getString(R.string.detail_tScr)} ${sdf.format(items[position].tScrSC)} - ${sdf.format(
                        items[position].tScrEC
                    )}"
            }
        }
        holder.number.text = numberText
        holder.name.text = items[position].name
        holder.screening.text = screening
        holder.risk.text = risk.toString().toUpperCase()
        holder.multiplicity.isChecked = items[position].multiplicity

        val comp = if (items[position].corr) {
            items[position].fScrDate
        } else {
            items[position].pm
        }
        val diff = ((cal.timeInMillis - comp.toLong()) / (24 * 60 * 60 * 1000)).toInt()
        var diffWeeks = diff / 7
        val diffDays = diff % 7
        if (items[position].corr) {
            diffWeeks += items[position].fScrTimeWeeks.toInt()
            if (items[position].fScrTimeDays.toInt() + diffDays > 6) {
                diffWeeks++
            }
        }
        when {
            diffWeeks < 20 -> holder.bg.setBackgroundColor(Color.CYAN)
            diffWeeks < 30 -> holder.bg.setBackgroundColor(Color.YELLOW)
            diffWeeks < 40 -> holder.bg.setBackgroundColor(Color.GREEN)
            else -> holder.bg.setBackgroundColor(Color.RED)
        }
    }

    fun dataChanged(newItems: List<DataModel>) {
        items = newItems
        notifyDataSetChanged()
    }
}