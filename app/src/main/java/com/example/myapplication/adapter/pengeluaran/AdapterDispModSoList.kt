package com.example.myapplication.adapter.pengeluaran

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModSoList
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AdapterDispModSoList(
    var context: Context, var list: List<ModelsDispModSoList>?, private var listener: OnAdapterListener)
    :RecyclerView.Adapter<AdapterDispModSoList.ItemHolder>(), Filterable {

    var tableList : List<ModelsDispModSoList> = mutableListOf()

    interface OnAdapterListener {
        fun onClick(list: ModelsDispModSoList)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvShipHeaderId : TextView = itemView.findViewById(R.id.ship_id)
        var tvShipNumber : TextView = itemView.findViewById(R.id.ship_number)
        var tvShipDate : TextView = itemView.findViewById(R.id.ship_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_dispatch_mod_so_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    @SuppressLint("SimpleDateFormat", "NewApi")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsDispModSoList = list!![position]
        holder.tvShipHeaderId.text = model.ship_header_id.toString()
        holder.tvShipDate.text = model.ship_date.toString()
        holder.tvShipNumber.text = model.ship_number.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(model)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence): FilterResults {
                val query = p0.toString()
                val filterResult = FilterResults()
                filterResult.values =
                    if(query.isEmpty()){
                        tableList
                    }else{
                        tableList.filter {
                            it.ship_number!!.contains(query, ignoreCase = true) || it.ship_number!!.contains(p0)
                        }
                    }
                return filterResult
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsDispModSoList>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList : MutableList<ModelsDispModSoList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }

}