package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.ActivityFaqBinding

class FaqActivity: AppCompatActivity() {

    lateinit var binding: ActivityFaqBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityFaqBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val faq = arrayListOf<Faq>(
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다"),
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다"),
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다"),
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다"),
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다"),
            Faq("공지사항 타이틀 예시입니다.", "공지사항 예시입니다")
        )

        binding.faqRv.apply {
            adapter = FaqAdapter().build(faq)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.backIv.setOnClickListener {
            finish()
        }
    }
}