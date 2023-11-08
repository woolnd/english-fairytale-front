package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityLoadingBinding
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat

class LoadingActivity: AppCompatActivity() {

    lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.beforebuttonLoadingIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent2 = Intent(this, CreateFinishActivity::class.java)
            startActivity(intent2)
        },2000)

    }
}