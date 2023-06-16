package com.example.myapplication.adapter.pengambilan.all

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
import com.example.myapplication.models.pengambilan.all.models.ModelsWaveList

class AdapterPickingAllModWaveList(var context: Context, var list: List<ModelsWaveList>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterPickingAllModWaveList.ItemHolder>(), Filterable{

    var tableList : List<ModelsWaveList> = mutableListOf()

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tWavePlanID : TextView = itemView.findViewById(R.id.wave_plan_id)
        var tSONumber : TextView = itemView.findViewById(R.id.no_so)
        var tCustName : TextView = itemView.findViewById(R.id.cust_name)
    }

    interface OnAdapterListener {
        fun onClick(list: ModelsWaveList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_picking_all_mod_wave_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsWaveList = list!![position]
        holder.tWavePlanID.text = model.wave_plan_id.toString()
        holder.tSONumber.text = model.no_so.toString()
        holder.tCustName.text = model.cust_name.toString()
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
                            it.wave_plan_id!!.contains(query, ignoreCase = true) || it.wave_plan_id!!.contains(p0)
                            it.no_so!!.contains(query, ignoreCase = true) || it.no_so!!.contains(p0)
                        }
                    }
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsWaveList>?
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItems(dataList: MutableList<ModelsWaveList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}