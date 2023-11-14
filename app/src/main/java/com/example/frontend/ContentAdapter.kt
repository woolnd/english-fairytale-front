package com.example.frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemContentBinding

class ContentAdapter: RecyclerView.Adapter<ContentAdapter.ViewHolder>(){

    lateinit var items: ArrayList<String>

    fun build(i: ArrayList<String>) : ContentAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemContentBinding, val context: Context): RecyclerView.ViewHolder(binding.root){
        fun bind(item: String) {
            binding.contentTv.text = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentAdapter.ViewHolder =
        ContentAdapter.ViewHolder(
            ItemContentBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            parent.context
        )

    override fun onBindViewHolder(holder: ContentAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

}