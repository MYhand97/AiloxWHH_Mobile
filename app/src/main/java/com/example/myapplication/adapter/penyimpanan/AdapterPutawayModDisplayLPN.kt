package com.example.myapplication.adapter.penyimpanan

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
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayModDisplayLPN

class AdapterPutawayModDisplayLPN(var context: Context, var list: List<ResponsePutawayModDisplayLPN>?)
    : RecyclerView.Adapter<AdapterPutawayModDisplayLPN.ItemHolder>(), Filterable {

    var tableList : List<ResponsePutawayModDisplayLPN> = mutableListOf()


    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemId: TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription: TextView = itemView.findViewById(R.id.item_description)
        var tvItemNumber: TextView = itemView.findViewById(R.id.item_number)
        var tvQty: TextView = itemView.findViewById(R.id.qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_putaway_mod_display_lpn, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responsePutawayModDisplayLPN: ResponsePutawayModDisplayLPN = list!![position]
        holder.tvItemId.text = responsePutawayModDisplayLPN.item_id.toString()
        holder.tvItemDescription.text = responsePutawayModDisplayLPN.item_description.toString()
        holder.tvItemNumber.text = responsePutawayModDisplayLPN.item_number.toString()
        holder.tvQty.text = responsePutawayModDisplayLPN.qty.toString()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence): FilterResults {
                val queryString = p0.toString()

                val filterResult = FilterResults()
                filterResult.values =
                    if(queryString.isEmpty()){
                        tableList
                    }else{
                        tableList.filter {
                            it.item_number!!.contains(queryString, ignoreCase = true) || it.item_number!!.contains(p0)
                        }
                    }
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ResponsePutawayModDisplayLPN>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList: MutableList<ResponsePutawayModDisplayLPN>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }
}