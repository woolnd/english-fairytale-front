package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityContactBinding

class ContactActivity: AppCompatActivity() {

    lateinit var binding: ActivityContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backIv.setOnClickListener {
            finish()
        }
    }
}