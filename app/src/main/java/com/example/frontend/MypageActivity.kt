package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityMypageBinding

class MypageActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMypageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val memberId = intent.getIntExtra("memberId", 0)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.infoBtnIv.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }

        binding.noticeIv.setOnClickListener {
            val intent = Intent(this, NotifyActivity::class.java)
            startActivity(intent)
        }

        binding.faqIv.setOnClickListener {
            val intent = Intent(this, FaqActivity::class.java)
            startActivity(intent)
        }

        binding.serviceIv.setOnClickListener {
            val intent = Intent(this, ServiceActivity::class.java)
            startActivity(intent)
        }

        binding.privacyIv.setOnClickListener {
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)
        }
    }
}