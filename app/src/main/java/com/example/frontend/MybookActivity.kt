package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityMybookBinding

class MybookActivity: AppCompatActivity() {

    lateinit var binding: ActivityMybookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMybookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener {
            finish()
        }
    }
}