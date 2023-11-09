package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var memberId = intent.getIntExtra("memberId", 0)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        setDataAtFragment(MainFragment(), memberId)

        binding.profileIv.setOnClickListener {
            val intent = Intent(this, MypageActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
    }

    //프래그먼트에 데이터 전달하기
    fun setDataAtFragment(fragment: Fragment, memberId:Int) {
        val bundle = Bundle()
        bundle.putInt("memberId", memberId)

        fragment.arguments = bundle
        setFragment(fragment)
    }

    //프래그먼트 띄우기
    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fl, fragment)
        transaction.commit()
    }
}