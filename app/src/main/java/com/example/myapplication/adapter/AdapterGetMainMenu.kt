package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.ResponseMainMenu

class AdapterGetMainMenu(var context: Context, var list: List<ResponseMainMenu>?, val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterGetMainMenu.ItemHolder>(){
    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvMainMenu: TextView = itemView.findViewById(R.id.subMenuTitle)
        var tvMainMenuId: TextView = itemView.findViewById(R.id.master_sub_menu_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.rows_menu_utama, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int){
        val responseMainMenu: ResponseMainMenu = list!![position]
        holder.tvMainMenu.text = responseMainMenu.sub_menu_title.toString()
        holder.tvMainMenuId.text = responseMainMenu.master_sub_menu_id.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(responseMainMenu)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface OnAdapterListener {
        fun onClick(list: ResponseMainMenu)
    }
}
