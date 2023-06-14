package com.example.myapplication.adapter.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseGetTmpReceive

@Suppress("UNCHECKED_CAST")
class AdapterEceranScanBarcode(var context: Context, var list: List<ResponseGetTmpReceive>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterEceranScanBarcode.ItemHolder>(), Filterable {

    var tableList : List<ResponseGetTmpReceive> = mutableListOf()
    val session = context.getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

    interface OnAdapterListener {
        fun onClick(list : ResponseGetTmpReceive)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemId: TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription: TextView = itemView.findViewById(R.id.item_description)
        var tvItemDesign: TextView = itemView.findViewById(R.id.item_design)
        var tvOrderedQty: TextView = itemView.findViewById(R.id.ordered_qty)
        var tvReceivedQty: TextView = itemView.findViewById(R.id.received_qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_penerimaan_eceran_item, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responseValItem: ResponseGetTmpReceive = list!![position]
        if(responseValItem.t_scan_qty!!.toInt() > responseValItem.kurang_qty!!.toInt()){
            holder.tvItemDescription.setTextColor(Color.parseColor("#FF0000"))
            holder.tvOrderedQty.setTextColor(Color.parseColor("#FF0000"))
            holder.tvReceivedQty.setTextColor(Color.parseColor("#FF0000"))
        }
        holder.tvItemDescription.text = responseValItem.item_barcode.toString()
        holder.tvItemId.text = responseValItem.item_id.toString()
        holder.tvOrderedQty.text = responseValItem.t_scan_qty.toString()
        holder.tvReceivedQty.text = responseValItem.kurang_qty.toString()
        holder.tvItemDesign.visibility = View.GONE
        holder.itemView.setOnClickListener {
            listener.onClick(responseValItem)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val queryString = charSequence.toString()

                val filterResults = FilterResults()
                filterResults.values =
                    if(queryString.isEmpty()){
                        tableList
                    }else{
                        tableList.filter {
                            it.item_barcode!!.contains(queryString, ignoreCase = true) || it.item_barcode!!.contains(charSequence)
                        }
                    }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                list = filterResults.values as List<ResponseGetTmpReceive>
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList: MutableList<ResponseGetTmpReceive>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}