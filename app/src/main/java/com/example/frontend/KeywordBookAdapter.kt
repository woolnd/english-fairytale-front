package com.example.frontend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemKeywordBookBinding

class KeywordBookAdapter : RecyclerView.Adapter<KeywordBookAdapter.ViewHolder>(){

    lateinit var items: ArrayList<String>

    fun build(i : ArrayList<String>) : KeywordBookAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemKeywordBookBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: String){
            binding.keywordTv.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordBookAdapter.ViewHolder = ViewHolder(
        ItemKeywordBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


}