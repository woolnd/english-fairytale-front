package com.example.frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontend.databinding.ActivityLoadingBinding
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoadingActivity: AppCompatActivity() {

    lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(0, TimeUnit.SECONDS) // 연결 제한 시간 설정 (0초로 설정하여 시간 제한 없음)
            .readTimeout(0, TimeUnit.SECONDS)    // 읽기 제한 시간 설정 (0초로 설정하여 시간 제한 없음)
            .addInterceptor(TokenInterceptor()) // Add your custom interceptor
            .build()


        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.27.113:8080")//서버 주소를 적을 것
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var Service = retrofit.create(Service::class.java)

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.beforebuttonLoadingIv.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val memberId = intent.getIntExtra("memberId", 0)
        val model = intent.getStringExtra("model")
        val textlist = intent.getStringArrayListExtra("textList")
        val imageStatus = intent.getStringExtra("imageStatus")

        val request = TaleCreateDto(memberId, model, textlist, imageStatus)
        Service.makeFairytale(request, null).enqueue(object : Callback<TaleResponse> {
            var dialog = AlertDialog.Builder(this@LoadingActivity)
            override fun onResponse(
                call: Call<TaleResponse>,
                response: Response<TaleResponse>
            ) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                if (result != null) {
                    val intent = Intent(this@LoadingActivity, BookActivity::class.java)
                    intent.putExtra("taleId", result.taleId)
                    startActivity(intent)
                    finish()

                } else {
                    dialog.setTitle("생성 실패")
                    dialog.setMessage("생성에 실패하였습니다.")
                    dialog.show()

                }
            }

            override fun onFailure(call: Call<TaleResponse>, t: Throwable) {
                dialog.setTitle("통신 실패")
                dialog.setMessage("통신에 실패하였습니다.")
                dialog.show()
            }
        })

    }
}
