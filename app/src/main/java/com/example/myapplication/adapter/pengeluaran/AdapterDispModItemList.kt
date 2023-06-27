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
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModItemList

class AdapterDispModItemList(
    var context: Context, var list: List<ModelsDispModItemList>?,
    private var listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterDispModItemList.ItemHolder>(), Filterable{

    var tableList : List<ModelsDispModItemList> = mutableListOf()

    interface OnAdapterListener {
        fun onClick(list: ModelsDispModItemList)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvPackDetailId : TextView = itemView.findViewById(R.id.pack_detail_id)
        var tvDispDetailId : TextView = itemView.findViewById(R.id.disp_detail_id)
        var tvItemId : TextView = itemView.findViewById(R.id.item_id)
        var tvItemNumber : TextView = itemView.findViewById(R.id.item_number)
        var tvPackQty : TextView = itemView.findViewById(R.id.pack_qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_dispatch_mod_item_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsDispModItemList = list!![position]
        holder.tvPackDetailId.text = model.pack_detail_id.toString()
        holder.tvDispDetailId.text = model.disp_detail_id.toString()
        holder.tvItemId.text = model.item_id.toString()
        holder.tvItemNumber.text = model.item_number.toString()
        holder.tvPackQty.text = model.pack_qty.toString()+" "+model.uom.toString()
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
                            it.item_number!!.contains(query, ignoreCase = true)
                                    || it.item_number!!.contains(p0)
                        }
                    }
                return filterResult
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsDispModItemList>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList : MutableList<ModelsDispModItemList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}