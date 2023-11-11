package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityServiceBinding

class ServiceActivity: AppCompatActivity() {

    lateinit var binding: ActivityServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityServiceBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.backIv.setOnClickListener {
            finish()
        }
    }
}