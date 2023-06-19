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
import com.example.myapplication.models.pengambilan.all.models.ModelsModLocList

class AdapterPickingAllModLocList(var context: Context, var list: List<ModelsModLocList>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterPickingAllModLocList.ItemHolder>(), Filterable {

    var tableList : List<ModelsModLocList> = mutableListOf()

    interface OnAdapterListener {
        fun OnClick(list: ModelsModLocList)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvWQID : TextView = itemView.findViewById(R.id.wq_id)
        var tvFromLocation : TextView = itemView.findViewById(R.id.from_location)
        var tvLocName : TextView = itemView.findViewById(R.id.loc_name)
        var tvQty : TextView = itemView.findViewById(R.id.qty)
        var tvQtyAmbil : TextView = itemView.findViewById(R.id.qty_ambil)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_picking_all_mod_loc_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsModLocList = list!![position]
        holder.tvWQID.text = model.wq_id.toString()
        holder.tvFromLocation.text = model.from_location.toString()
        holder.tvLocName.text = model.loc_name.toString()
        holder.tvQty.text = model.qty.toString()
        holder.tvQtyAmbil.text = model.qty_ambil.toString()
        holder.itemView.setOnClickListener {
            listener.OnClick(model)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence): FilterResults {
                val query = p0.toString()
                val filterResult = FilterResults()
                filterResult.values =
                    if(query.isEmpty()){
                        tableList
                    } else {
                        tableList.filter {
                            it.loc_name!!.contains(query, ignoreCase = true) || it.loc_name!!.contains(p0)
                        }
                    }
                return filterResult
            }
            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsModLocList>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList: MutableList<ModelsModLocList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}