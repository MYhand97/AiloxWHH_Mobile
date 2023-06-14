package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.ResponseSubMenu

class AdapterGetSubMenu(var context: Context, var list: List<ResponseSubMenu>?, val listener: OnAdapterListener)
    :RecyclerView.Adapter<AdapterGetSubMenu.ItemHolder>(){
    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var tvSubMenu: TextView = itemView.findViewById(R.id.submenu_title)
        var tbSubMenuId: TextView = itemView.findViewById(R.id.submenu_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.rows_sub_menu, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val responseSubMenu: ResponseSubMenu = list!![position]
        holder.tvSubMenu.text = responseSubMenu.sub_menu_title.toString()
        holder.tbSubMenuId.text = responseSubMenu.sub_menu_id.toString()
        holder.itemView.setOnClickListener {
            listener.onClick(responseSubMenu)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface OnAdapterListener{
        fun onClick(list: ResponseSubMenu)
    }
}