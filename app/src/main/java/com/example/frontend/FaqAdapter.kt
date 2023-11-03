package com.example.frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemFaqBinding

class FaqAdapter: RecyclerView.Adapter<FaqAdapter.ViewHolder>(){

    lateinit var items: ArrayList<Faq>

    fun build(i: ArrayList<Faq>) : FaqAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemFaqBinding, val context: Context): RecyclerView.ViewHolder(binding.root){
        fun bind(item: Faq){
            with(binding){
                titleTv.text = item.title
                contentTv.text = item.content

                titleCl.setOnClickListener {
                    if(contentCl.visibility == View.GONE){
                        contentCl.visibility = View.VISIBLE
                        arrowIv.setImageResource(R.drawable.faq_arrow1)
                    }else if(contentCl.visibility == View.VISIBLE){
                        contentCl.visibility = View.GONE
                        arrowIv.setImageResource(R.drawable.faq_arrow)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemFaqBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}