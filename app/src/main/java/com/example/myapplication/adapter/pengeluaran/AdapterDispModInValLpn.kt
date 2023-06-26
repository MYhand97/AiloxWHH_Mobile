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
import com.example.myapplication.models.pengeluaran.pack.models.ModelsDispModInValLpn

class AdapterDispModInValLpn(
    var context: Context, var list: List<ModelsDispModInValLpn>?, private var listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterDispModInValLpn.ItemHolder>(), Filterable {

    var tableList : List<ModelsDispModInValLpn> = mutableListOf()

    interface OnAdapterListener {
        fun onClick(list: ModelsDispModInValLpn)
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvWavePlanId : TextView = itemView.findViewById(R.id.wave_plan_ids)
        var tvPackLpnId : TextView = itemView.findViewById(R.id.pack_lpn_id)
        var tvOpdPackQty : TextView = itemView.findViewById(R.id.opd_pack_qty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.rows_dispatch_mod_in_val_lpn, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val model : ModelsDispModInValLpn = list!![position]
        holder.tvWavePlanId.text = model.wave_plans_id.toString()
        holder.tvPackLpnId.text = model.pack_lpn_id.toString()
        holder.tvOpdPackQty.text = model.opd_pack_qty.toString()+" "+model.uom.toString()
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
                            it.pack_lpn_id!!.contains(query, ignoreCase = true)
                                    || it.pack_lpn_id!!.contains(p0)
                        }
                    }
                return filterResult
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults) {
                list = p1.values as List<ModelsDispModInValLpn>?
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItemList(dataList : MutableList<ModelsDispModInValLpn>){
        tableList = dataList.toMutableList()
        list = tableList
        notifyDataSetChanged()
    }

}