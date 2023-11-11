package com.example.frontend

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.ItemBooksBinding

class CommunityAdapter : RecyclerView.Adapter<CommunityAdapter.ViewHolder>(){
    lateinit var items: ArrayList<Books>

    fun build(i: ArrayList<Books>) : CommunityAdapter{
        items = i
        return this
    }

    class ViewHolder(val binding: ItemBooksBinding, val context: Context) : RecyclerView.ViewHolder(binding.root){


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
                        bookEx1Iv.setImageResource(R.drawable.community_book)
                        bookEx1BtnIv.setImageResource(R.drawable.start_btn1)
                    }
                    2->{
                        bookEx1Iv.setImageResource(R.drawable.community_book1)
                        bookEx1BtnIv.setImageResource(R.drawable.start_btn2)
                    }
                    3->{
                        bookEx1Iv.setImageResource(R.drawable.community_book2)
                        bookEx1BtnIv.setImageResource(R.drawable.start_btn3)
                    }
                    4->{
                        bookEx1Iv.setImageResource(R.drawable.community_book3)
                        bookEx1BtnIv.setImageResource(R.drawable.start_btn4)
                    }
                    5->{
                        bookEx1Iv.setImageResource(R.drawable.community_book4)
                        bookEx1BtnIv.setImageResource(R.drawable.start_btn5)
                    }

                }

                if(item.heart.toString() == "true"){
                    heartIv.setImageResource(R.drawable.heart)
                }
                else{
                    heartIv.setImageResource(R.drawable.blank_heart)
                }

                //하트클릭 시 이미지 변환
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityAdapter.ViewHolder = ViewHolder(
        ItemBooksBinding.inflate(LayoutInflater.from(parent.context), parent, false), parent.context)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}