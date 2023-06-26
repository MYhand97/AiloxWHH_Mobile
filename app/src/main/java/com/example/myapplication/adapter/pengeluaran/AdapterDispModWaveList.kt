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
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModWaveList

class AdapterDispModWaveList(
    var context : Context, var list: List<ModelsDispModWaveList>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterDispModWaveList.ItemHolder>(), Filterable {

    var tableList : List<ModelsDispModWaveList> = mutableListOf()

    interface OnAdapterListener {
        fun onClick(list : ModelsDispModWaveList)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvCustId : TextView = itemView.findViewById(R.id.cust_id)
        var tvCustCode : TextView = itemView.findViewById(R.id.cust_code)
        var tvCustName : TextView = itemView.findViewById(R.id.cust_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_dispatch_mod_wave_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsDispModWaveList = list!![position]
        holder.tvCustId.text = model.ship_cust_id.toString()
        holder.tvCustCode.text = model.cust_code.toString()
        holder.tvCustName.text = model.cust_name.toString()
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
                            it.cust_name!!.contains(query, ignoreCase = true) || it.cust_name!!.contains(p0)
                        }
                    }
                return filterResult
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsDispModWaveList>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList: MutableList<ModelsDispModWaveList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }

}