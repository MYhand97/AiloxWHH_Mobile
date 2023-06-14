package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.models.ModelsSelectDB

class AdapterSelectDB(var context: Context, var list: List<ModelsSelectDB>?, val listener: OnAdapterListener)
    : RecyclerView.Adapter<AdapterSelectDB.ItemHolder>() {

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvdbSeq: TextView = itemView.findViewById(R.id.dbSeq)
        var tvdbName: TextView = itemView.findViewById(R.id.dbName)
        var tvdbTitle: TextView = itemView.findViewById(R.id.dbTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(parent.context).inflate(R.layout.rows_select_db, parent, false)
        return ItemHolder(itemHolder)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val modelSelectDB: ModelsSelectDB = list!![position]

        holder.tvdbSeq.text = modelSelectDB.db_seq.toString()
        holder.tvdbName.text = modelSelectDB.db_name.toString()
        holder.tvdbTitle.text = modelSelectDB.db_title.toString()

        holder.itemView.setOnClickListener{
            listener.onClick(modelSelectDB)
            //Toast.makeText(context, modelSelectDB.db_name.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    interface OnAdapterListener{
        fun onClick(list: ModelsSelectDB)
    }
}