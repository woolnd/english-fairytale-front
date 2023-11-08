package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityCreateFinishBinding

class CreateFinishActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateFinishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateFinishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.beforebuttonFinishIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.viewBookIv.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }
    }
}