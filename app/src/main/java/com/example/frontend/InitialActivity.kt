package com.example.frontend

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityInitialBinding

class InitialActivity: AppCompatActivity() {
    lateinit var binding: ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInitialBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                val currentImg = binding.backgroundIv.drawable
                val back1 = ContextCompat.getDrawable(this@InitialActivity, R.drawable.initial_back1)
                val back2 = ContextCompat.getDrawable(this@InitialActivity, R.drawable.initial_back2)
                if (currentImg != null && back1 != null && back2 != null) {
                    if (areDrawablesEqual(currentImg, back1)) {
                        binding.backgroundIv.setImageResource(R.drawable.initial_back2)
                    } else if (areDrawablesEqual(currentImg, back2)) {
                        binding.backgroundIv.setImageResource(R.drawable.initial_back1)
                    }
                }
                handler.postDelayed(this, 3000)
            }
        }

// 처음 실행 예약
        handler.postDelayed(runnable, 3000)

        binding.joinTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.pwTv.setOnClickListener {
            val intent = Intent(this, ResetPwActivity::class.java)
            startActivity(intent)
        }

        binding.loginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
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