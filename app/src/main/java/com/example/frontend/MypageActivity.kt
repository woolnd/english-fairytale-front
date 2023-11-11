package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityMypageBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MypageActivity: AppCompatActivity() {

    lateinit var binding: ActivityMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMypageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor()) // Add your custom interceptor
            .build()


        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.27.113:8080")//서버 주소를 적을 것
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var Service = retrofit.create(Service::class.java)


        val memberId = intent.getIntExtra("memberId", 0)

        Service.detailInfo(memberId).enqueue(object : Callback<DetailInfoResponse> {
            override fun onResponse(call: Call<DetailInfoResponse>, response: Response<DetailInfoResponse>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                var dialog = AlertDialog.Builder(this@MypageActivity)
                var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                if(result != null){
                    binding.profileTv.text = result.nickname
                }
                else{
                    dialog.setTitle("조회 실패")
                    dialog.setMessage("조회에 실패하였습니다.")
                    dialog.show()

                }
            }
            override fun onFailure(call: Call<DetailInfoResponse>, t: Throwable) {

            }
        })

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.infoBtnIv.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }

        binding.inquiryBtnIv.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
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