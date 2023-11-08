package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityLikebookBinding

class LikebookActivity: AppCompatActivity() {

    lateinit var binding: ActivityLikebookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikebookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIv.setOnClickListener {
            finish()
        }
    }
}