package com.example.myapplication.adapter.penerimaan.penerimaaneceran

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.penerimaan.penerimaaneceran.ResponseValItem

@Suppress("UNCHECKED_CAST")
class AdapterEceranValItem(var context: Context, var list: List<ResponseValItem>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterEceranValItem.ItemHolder>(), Filterable {

    var tableList : List<ResponseValItem> = mutableListOf()
    val session = context.getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemId: TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription: TextView = itemView.findViewById(R.id.item_description)
        var tvItemDesign: TextView = itemView.findViewById(R.id.item_design)
        var tvOrderedQty: TextView = itemView.findViewById(R.id.ordered_qty)
        var tvReceivedQty: TextView = itemView.findViewById(R.id.received_qty)
    }

    interface OnAdapterListener {
        fun onClick(list : ResponseValItem)
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
        val responseValItem: ResponseValItem = list!![position]
        when(session.getString("pp_mode", null)){
            "Single" -> {
                holder.tvItemDescription.text = responseValItem.item_design.toString()
                holder.tvItemDesign.text = responseValItem.item_description.toString()
            }
            "Multi" -> {
                holder.tvItemDescription.text = responseValItem.item_design.toString()
                holder.tvItemDesign.text = responseValItem.item_description.toString()
            }
            else -> {
                holder.tvItemDescription.text = responseValItem.item_barcode.toString()
                holder.tvItemDesign.text = responseValItem.item_description.toString()
            }
        }
        holder.tvItemId.text = responseValItem.item_id.toString()
        holder.tvOrderedQty.text = responseValItem.t_rcpt_ordered_qty.toString()
        holder.tvReceivedQty.text = responseValItem.t_rcpt_received_qty.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(responseValItem)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList: MutableList<ResponseValItem>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
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
                        if(session.getString("pp_mode", null) == "Single" || session.getString("pp_mode", null) == "Multi"){
                            tableList.filter {
                                it.item_design!!.contains(queryString, ignoreCase = true) || it.item_design!!.contains(charSequence)
                            }
                        }else{
                            tableList.filter {
                                it.item_barcode!!.contains(queryString, ignoreCase = true) || it.item_barcode!!.contains(charSequence)
                            }
                        }
                    }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                list = filterResults.values as List<ResponseValItem>?
                notifyDataSetChanged()
            }
        }
    }
}