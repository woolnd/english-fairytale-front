package com.example.frontend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemSearchBinding

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    lateinit var items: ArrayList<Search>


    fun build(i: ArrayList<Search>) : SearchAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemSearchBinding, val context: Context): RecyclerView.ViewHolder(binding.root){

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

        fun bind(item: Search){
            with(binding){
                titleTv.text = item.title
                keywordTv.text = item.keywords
                nickTv.text = item.nick

                if(item.heart.toString() == "true"){
                    heartIv.setImageResource(R.drawable.heart)
                }
                else{
                    heartIv.setImageResource(R.drawable.blank_heart)
                }

                heartIv.setOnClickListener {
                    val currentImg = binding.heartIv.drawable
                    val heart = ContextCompat.getDrawable(context, R.drawable.heart)
                    val blank2heart = ContextCompat.getDrawable(context, R.drawable.blank2_heart)
                    if(currentImg != null && heart != null && blank2heart != null){
                        if(areDrawablesEqual(currentImg, heart)){
                            binding.heartIv.setImageResource(R.drawable.blank2_heart)
                        }
                        else if(areDrawablesEqual(currentImg, blank2heart)){
                            binding.heartIv.setImageResource(R.drawable.heart)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}