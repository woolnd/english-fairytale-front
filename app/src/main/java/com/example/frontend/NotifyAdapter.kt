package com.example.frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemNotifyBinding

class NotifyAdapter: RecyclerView.Adapter<NotifyAdapter.ViewHolder>() {
    lateinit var items: ArrayList<Notify>

    fun build(i: ArrayList<Notify>) : NotifyAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemNotifyBinding, val context: Context): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Notify){
            with(binding){
                titleTv.text = item.title
                dateTv.text= item.date
                contentTv.text = item.content
                titleCl.setOnClickListener {
                    if(contentCl.visibility == View.GONE){
                        contentCl.visibility = View.VISIBLE
                    }else if(contentCl.visibility == View.VISIBLE){
                        contentCl.visibility = View.GONE
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemNotifyBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}