package com.example.myapplication.adapter.pengambilan.all

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.models.pengambilan.all.models.ModelsLocItemList

class AdapterPickingAllModLocItemList(var context: Context, var list: List<ModelsLocItemList>?, private val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterPickingAllModLocItemList.ItemHolder>(), Filterable {

    var tableList : List<ModelsLocItemList> = mutableListOf()

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnAdapterListener {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}