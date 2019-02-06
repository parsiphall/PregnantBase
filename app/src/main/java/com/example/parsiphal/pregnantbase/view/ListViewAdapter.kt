package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ListViewAdapter(
    private var items: List<DataModel>,
    private val context: Context?
) : RecyclerView.Adapter<ListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    private val ad = AlertDialog.Builder(context!!)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
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
        holder.delete.setOnClickListener {
            ad.setTitle(items[position].name)
            ad.setMessage(context!!.getString(R.string.adMessage))
            val btn1 = context.getString(R.string.adBtn1)
            val btn2 = context.getString(R.string.adBtn2)
            ad.setPositiveButton(btn1) { _, _ ->
                GlobalScope.launch {
                    deleteData(position)
                }
            }
            ad.setNegativeButton(btn2) { dialog, _ ->
                dialog.cancel()
            }
            ad.show()
        }
        if (!items[position].release) {
            val comp = if (items[position].corr) {
                items[position].fScrDate
            } else {
                items[position].pm
            }
            val diff = ((cal.timeInMillis - comp.toLong()) / (24 * 60 * 60 * 1000)).toInt()
            val diffWeeks = diff / 7
            when {
                diffWeeks < 20 -> holder.bg.setBackgroundColor(Color.CYAN)
                diffWeeks < 30 -> holder.bg.setBackgroundColor(Color.YELLOW)
                diffWeeks < 40 -> holder.bg.setBackgroundColor(Color.GREEN)
                else -> holder.bg.setBackgroundColor(Color.RED)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.list_tab, parent, false)
        )
    }

    fun dataChanged(newItems: List<DataModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    private suspend fun deleteData(position: Int) {
        DB.getDao().deleteData(items[position])
        items = DB.getDao().getDataPregnant()
        MainScope().launch {
            dataChanged(items)

        }
    }
}