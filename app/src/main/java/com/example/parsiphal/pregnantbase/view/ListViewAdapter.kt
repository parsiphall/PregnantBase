package com.example.parsiphal.pregnantbase.view

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.parsiphal.pregnantbase.R
import com.example.parsiphal.pregnantbase.data.DataModel
import java.text.SimpleDateFormat

class ListViewAdapter(
    private var items: List<DataModel>,
    private val context: Context?
) : RecyclerView.Adapter<ListViewHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    private val ad = AlertDialog.Builder(context!!)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val numberText = "${(position + 1)}/${items.size}"
        val risk = items[position].riskText[0]
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var screening = ""
        if (!items[position].fScrC) {
            screening = "${context!!.resources.getString(R.string.detail_fScr)} ${sdf.format(items[position].fScrS)} - ${sdf.format(items[position].fScrE)}"
        } else if (!items[position].sScrC) {
            screening = "${context!!.resources.getString(R.string.detail_sScr)} ${sdf.format(items[position].sScrS)} - ${sdf.format(items[position].sScrE)}"
            if (items[position].corr) {
                screening = "${context.resources.getString(R.string.detail_sScr)} ${sdf.format(items[position].sScrSC)} - ${sdf.format(items[position].sScrEC)}"
            }
        } else if (!items[position].tScrC) {
            screening = "${context!!.resources.getString(R.string.detail_tScr)} ${sdf.format(items[position].tScrS)} - ${sdf.format(items[position].tScrE)}"
            if (items[position].corr) {
                screening = "${context.resources.getString(R.string.detail_tScr)} ${sdf.format(items[position].tScrSC)} - ${sdf.format(items[position].tScrEC)}"
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
                DB.getDao().deleteData(items[position])
                items = DB.getDao().getDataPregnant()
                dataChanged(items)
            }
            ad.setNegativeButton(btn2) { dialog, _ ->
                dialog.cancel()
            }
            ad.show()
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
}