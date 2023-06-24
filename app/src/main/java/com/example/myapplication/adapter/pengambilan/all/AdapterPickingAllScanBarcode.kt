package com.example.myapplication.adapter.pengambilan.all

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.pengambilan.all.models.ModelsTmpPick

class AdapterPickingAllScanBarcode(
    var context : Context, var list: List<ModelsTmpPick>?,
    private val listener: OnAdapterListener):
    RecyclerView.Adapter<AdapterPickingAllScanBarcode.ItemHolder>(), Filterable {

    var tableList : List<ModelsTmpPick> = mutableListOf()
    val session : SharedPreferences = context.getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

    interface OnAdapterListener {
        fun onClick(list : ModelsTmpPick)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemId : TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription : TextView = itemView.findViewById(R.id.item_description)
        var tvItemDesign : TextView = itemView.findViewById(R.id.item_design)
        var tvOrderedQty : TextView = itemView.findViewById(R.id.ordered_qty)
        var tvReceivedQty : TextView = itemView.findViewById(R.id.received_qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_picking_all_mod_loc_item_list, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val response : ModelsTmpPick = list!![position]
        /*if(response.t_scan_qty!!.toInt() > response.available_qty!!.toInt()){
            holder.tvItemDescription.setTextColor(Color.parseColor("#FF0000"))
            holder.tvOrderedQty.setTextColor(Color.parseColor("#FF0000"))
            holder.tvReceivedQty.setTextColor(Color.parseColor("#FF0000"))
        }*/
        holder.tvItemDescription.text = response.item_barcode.toString()
        holder.tvItemId.text = response.item_id.toString()
        holder.tvItemDesign.visibility = View.GONE
        holder.tvOrderedQty.text = response.t_scan_qty.toString()
        holder.tvReceivedQty.text = response.t_picked_qty.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(response)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence): FilterResults {
                val query = p0.toString()
                val result = FilterResults()
                result.values =
                    if(query.isEmpty()){
                        tableList
                    }else{
                        tableList.filter {
                            it.item_barcode!!.contains(query, ignoreCase = true) || it.item_barcode!!.contains(p0)
                        }
                    }
                return result
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsTmpPick>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList : MutableList<ModelsTmpPick>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}