package com.example.myapplication.adapter.penerimaan

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
import com.example.myapplication.models.penerimaan.ResponsePenerimaanEceran

@Suppress("UNCHECKED_CAST")
class AdapterPenerimaanEceran(var context: Context, var list: List<ResponsePenerimaanEceran>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterPenerimaanEceran.ItemHolder>(), Filterable{

    var tableList: List<ResponsePenerimaanEceran> = mutableListOf()

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvRcptHeaderId: TextView = itemView.findViewById(R.id.rcpt_header_id)
        var tvRcptNumber: TextView = itemView.findViewById(R.id.rcpt_number)
    }

    interface OnAdapterListener{
        fun onClick(list: ResponsePenerimaanEceran)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_penerimaan_eceran, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responsePenerimaanEceran: ResponsePenerimaanEceran = list!![position]
        holder.tvRcptHeaderId.text = responsePenerimaanEceran.rcpt_header_id.toString()
        holder.tvRcptNumber.text = responsePenerimaanEceran.rcpt_number.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(responsePenerimaanEceran)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListItems(dataList: MutableList<ResponsePenerimaanEceran>){
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
                            it.rcpt_number!!.contains(queryString, ignoreCase = true) || it.rcpt_number!!.contains(charSequence)
                        }
                    }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                list = filterResults.values as List<ResponsePenerimaanEceran>?
                notifyDataSetChanged()
            }
        }
    }
}