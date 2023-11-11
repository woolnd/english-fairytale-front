package com.example.frontend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemBestBinding

class BestAdapter : RecyclerView.Adapter<BestAdapter.ViewHolder>(){
    lateinit var items: ArrayList<Books>

    fun build(i: ArrayList<Books>) : BestAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemBestBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){


        // Drawable 객체를 비교하는 함수
        fun areDrawablesEqual(drawable1: Drawable, drawable2: Drawable): Boolean {
            val bitmap1 = drawableToBitmap(drawable1)
            val bitmap2 = drawableToBitmap(drawable2)
            return bitmap1.sameAs(bitmap2)
        }


        // Drawable을 Bitmap으로 변환하는 함수
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            if (drawable is BitmapDrawable) {
                return drawable.bitmap
            }

            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)

            return bitmap
        }
        fun bind(item: Books){

            with(binding){
                titleTv.text = item.title
                nickTv.text = item.nick
                when(item.color){
                    1 -> {
                        bestIv.setImageResource(R.drawable.best_card)
                    }
                    2 -> {
                        bestIv.setImageResource(R.drawable.best_card1)
                    }
                    3 -> {
                        bestIv.setImageResource(R.drawable.best_card2)
                    }
                    4 -> {
                        bestIv.setImageResource(R.drawable.best_card3)
                    }
                    5 -> {
                        bestIv.setImageResource(R.drawable.best_card4)
                    }

                }

                if(item.heart.toString() == "true"){
                    heartIv.setImageResource(R.drawable.heart)
                }
                else{
                    heartIv.setImageResource(R.drawable.blank_heart)
                }

                binding.heartIv.setOnClickListener {
                    val currentImg = binding.heartIv.drawable
                    val heart = ContextCompat.getDrawable(context, R.drawable.heart)
                    val blankheart = ContextCompat.getDrawable(context, R.drawable.blank_heart)
                    if(currentImg != null && heart != null && blankheart != null){
                        if(areDrawablesEqual(currentImg, heart)){
                            binding.heartIv.setImageResource(R.drawable.blank_heart)
                        }
                        else if(areDrawablesEqual(currentImg, blankheart)){
                            binding.heartIv.setImageResource(R.drawable.heart)
                        }
                    }
                }

                keywordRv.apply {
                    adapter = KeywordAdapter().build(item.keywords)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestAdapter.ViewHolder = ViewHolder(
        ItemBestBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}