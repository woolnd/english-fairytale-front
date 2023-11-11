package com.example.frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.ActivityNotifyBinding

class NotifyActivity: AppCompatActivity() {

    lateinit var binding: ActivityNotifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        val notify = arrayListOf<Notify>(
            Notify("공지사항 타이틀 예시입니다.", "2023.07.03", "공지사항 예시입니다"),
            Notify("공지사항 타이틀 예시입니다.", "2023.07.03", "공지사항 예시입니다"),
            Notify("공지사항 타이틀 예시입니다.", "2023.07.03", "공지사항 예시입니다"),
            Notify("공지사항 타이틀 예시입니다.", "2023.07.03", "공지사항 예시입니다"),
        )

        binding.notifyRv.apply {
            adapter = NotifyAdapter().build(notify)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.backIv.setOnClickListener {
            finish()
        }
    }
}