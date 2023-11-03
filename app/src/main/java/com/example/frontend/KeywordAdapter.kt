package com.example.frontend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemKeywordBinding

class KeywordAdapter : RecyclerView.Adapter<KeywordAdapter.ViewHolder>(){

    lateinit var items: ArrayList<String>

    fun build(i : ArrayList<String>) : KeywordAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemKeywordBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: String){
            binding.keywordTv.text = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordAdapter.ViewHolder = ViewHolder(
        ItemKeywordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


}