package com.example.frontend

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivityBookBinding
import java.util.Locale

class BookActivity: AppCompatActivity() {
    lateinit var binding: ActivityBookBinding
    private var textToSpeech: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment_book  = BookFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_fl, fragment_book).commit()

        binding.primary.setOnClickListener {
            val currentImg = binding.primary.drawable
            val translateOn = ContextCompat.getDrawable(this, R.drawable.book_primary_after)
            val translateOff = ContextCompat.getDrawable(this, R.drawable.book_primary)
            if(currentImg != null && translateOn != null && translateOff != null){
                if(areDrawablesEqual(currentImg, translateOff)){
                    binding.primary.setImageResource(R.drawable.book_primary_after)
                }
                else if(areDrawablesEqual(currentImg, translateOn)){
                    binding.primary.setImageResource(R.drawable.book_primary)
                }
            }
        }

        binding.english.setOnClickListener {

        }
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }

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
}