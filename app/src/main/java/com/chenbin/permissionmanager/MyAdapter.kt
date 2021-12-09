package com.chenbin.permissionmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author chenbin
 * @date 12/8/21
 */
class MyAdapter(private val mContext: Context, private val mData: List<ItemBean>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val status: TextView = itemView.findViewById(R.id.status)
    }

    private var onItemClick: (() -> Unit) ?= null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = mData[position]
        holder.name.text = data.name
        holder.status.text = if (data.granted) "已开启" else "未开启"
        holder.itemView.setOnClickListener {
            onItemClick?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClick(itemClick: () -> Unit) {
        onItemClick = itemClick
    }
}