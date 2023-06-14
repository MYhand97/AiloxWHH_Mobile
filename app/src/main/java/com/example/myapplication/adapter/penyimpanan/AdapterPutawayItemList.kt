package com.example.myapplication.adapter.penyimpanan

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetItemInLPN

@Suppress("UNCHECKED_CAST")
class AdapterPutawayItemList(var context: Context, var list: List<ResponsePutawayGetItemInLPN>?)
    : RecyclerView.Adapter<AdapterPutawayItemList.ItemHolder>(), Filterable {

    var tableList : List<ResponsePutawayGetItemInLPN> = mutableListOf()
    val session = context.getSharedPreferences("ailoxwms_data", MODE_PRIVATE)

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvItemId: TextView = itemView.findViewById(R.id.item_id)
        var tvItemDescription: TextView = itemView.findViewById(R.id.item_description)
        var tvItemNumber: TextView = itemView.findViewById(R.id.item_number)
        var tvQty: TextView = itemView.findViewById(R.id.qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_penyimpanan_putaway_item_in_lpn, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responsePutawayGetItemInLPN: ResponsePutawayGetItemInLPN = list!![position]
        holder.tvItemId.text = responsePutawayGetItemInLPN.item_id.toString()
        holder.tvItemNumber.text = responsePutawayGetItemInLPN.item_number.toString()
        holder.tvItemDescription.text = responsePutawayGetItemInLPN.item_description.toString()
        holder.tvQty.text = responsePutawayGetItemInLPN.qty.toString()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList: MutableList<ResponsePutawayGetItemInLPN>){
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
                        tableList.filter {
                            it.item_number!!.contains(queryString, ignoreCase = true) || it.item_number!!.contains(charSequence)
                        }
                    }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                list = filterResults.values as List<ResponsePutawayGetItemInLPN>?
                notifyDataSetChanged()
            }
        }
    }
}