package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var memberId = intent.getIntExtra("memberId", 0)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        supportFragmentManager.beginTransaction().add(R.id.main_fl, MainFragment())
            .commit()

        binding.profileIv.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
    }
}