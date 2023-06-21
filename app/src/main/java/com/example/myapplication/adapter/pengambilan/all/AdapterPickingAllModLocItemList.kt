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
import com.example.myapplication.models.pengambilan.all.models.ModelsLocItemList
import com.example.myapplication.models.pengambilan.all.models.ModelsModLocList

class AdapterPickingAllModLocItemList(var context: Context, var list: List<ModelsLocItemList>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterPickingAllModLocItemList.ItemHolder>(), Filterable {

    var tableList : List<ModelsLocItemList> = mutableListOf()

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemID : TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription : TextView = itemView.findViewById(R.id.item_description)
        var tvItemNumber : TextView = itemView.findViewById(R.id.item_number)
        var tvQty : TextView = itemView.findViewById(R.id.qty)
        var tvQtyAmbil : TextView = itemView.findViewById(R.id.qty_ambil)
    }

    interface OnAdapterListener {
        fun onClick(list: ModelsLocItemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_picking_mod_loc_item_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsLocItemList = list!![position]
        holder.tvItemID.text = model.item_id.toString()
        holder.tvItemDescription.text = model.item_description.toString()
        holder.tvItemNumber.text = model.item_number.toString()
        holder.tvQty.text = model.qty.toString()
        holder.tvQtyAmbil.text = model.qty_ambil.toString()
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
                            it.item_description!!.contains(query, ignoreCase = true) || it.item_description!!.contains(query)
                            it.item_number!!.contains(query, ignoreCase = true)|| it.item_number!!.contains(query)
                        }
                    }
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsLocItemList>?
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList: MutableList<ModelsLocItemList>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }

}