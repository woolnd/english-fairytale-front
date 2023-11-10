package com.example.frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemSentenceBinding

class SentenceAdapter : RecyclerView.Adapter<SentenceAdapter.ViewHolder>(){
    lateinit var items: ArrayList<String>

    fun build(i: ArrayList<String>) : SentenceAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemSentenceBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: String){

            with(binding){
                textTv.text = item
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentenceAdapter.ViewHolder = ViewHolder(
        ItemSentenceBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}