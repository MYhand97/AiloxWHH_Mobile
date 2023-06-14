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
import com.example.myapplication.models.penyimpanan.putawaybylpn.ResponsePutawayGetMasterLocation

class AdapterPutawayMasterLocation(var context: Context, var list: List<ResponsePutawayGetMasterLocation>?, private var listener: onAdapterListener)
    : RecyclerView.Adapter<AdapterPutawayMasterLocation.ItemHolder>(), Filterable {

    var tableList: List<ResponsePutawayGetMasterLocation> = mutableListOf()

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvLocCD : TextView = itemView.findViewById(R.id.loc_cd)
        var tvLocName : TextView = itemView.findViewById(R.id.loc_name)
    }

    interface onAdapterListener {
        fun onClick(list : ResponsePutawayGetMasterLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_putaway_mod_locating_manual, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responsePutawayGetMasterLocation: ResponsePutawayGetMasterLocation = list!![position]
        holder.tvLocCD.text = responsePutawayGetMasterLocation.loc_cd.toString()
        holder.tvLocName.text = responsePutawayGetMasterLocation.loc_name.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(responsePutawayGetMasterLocation)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(p0: CharSequence): FilterResults {
                val queryString = p0.toString()
                val filterResult = FilterResults()
                filterResult.values =
                    if(queryString.isEmpty()){
                        tableList
                    } else {
                        tableList.filter {
                            it.loc_name!!.contains(queryString, ignoreCase = true) || it.loc_name!!.contains(p0)
                        }
                    }
                return filterResult
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ResponsePutawayGetMasterLocation>?
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(dataList: MutableList<ResponsePutawayGetMasterLocation>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }

}