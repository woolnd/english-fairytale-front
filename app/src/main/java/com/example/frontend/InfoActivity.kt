package com.example.frontend

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.frontend.databinding.ActivityInfoBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class InfoActivity: AppCompatActivity(){
    lateinit var binding: ActivityInfoBinding

    var nick: String =""
    var newNick: String =""
    var userId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInfoBinding.inflate(layoutInflater)
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

        userId = intent.getIntExtra("memberId", 0)

        Service.detailInfo(userId).enqueue(object : Callback<DetailInfoResponse> {
            override fun onResponse(call: Call<DetailInfoResponse>, response: Response<DetailInfoResponse>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                var dialog = AlertDialog.Builder(this@InfoActivity)
                var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                if(result != null){
                    binding.emailTv.text = result.email
                    binding.nickEt.hint = result.nickname
                    nick = result.nickname
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

        val fragment_profile = InfoProfilePopupFragment()
        val fragment_popup = InfoPopupFragment()



        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        binding.btnIv.setOnClickListener {
            newNick = binding.nickEt.text.toString()
            var dialog = AlertDialog.Builder(this@InfoActivity)

            if(newNick == ""){
                supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
                window.statusBarColor = ContextCompat.getColor(this@InfoActivity, R.color.translucent_gray)
            }
            else{
                Service.modifyNick(userId, newNick).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                        var result = response.body()
                        if(result!=null){
                            supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
                            window.statusBarColor = ContextCompat.getColor(this@InfoActivity, R.color.translucent_gray)
                        }else{
                            dialog.setTitle("수정 실패")
                            dialog.setMessage("수정에 실패하였습니다.")
                            dialog.show()

                        }
                    }
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        //웹통신이 실패했을 시
                        dialog.setTitle("통신 실패")
                        dialog.setMessage("${t}")
                        dialog.show()
                    }
                })
            }
        }

        binding.pwBoxCl.setOnClickListener {
            val intent = Intent(this, PwModifyActivity::class.java)
            intent.putExtra("memberId", userId)
            startActivity(intent)
        }


        binding.profileSettingIv.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_profile).commit()
            window.statusBarColor = ContextCompat.getColor(this, R.color.translucent_gray)
        }


        binding.nickBtnIv.setOnClickListener {

            var nick = binding.nickEt.text.toString()
            var dialog = AlertDialog.Builder(this@InfoActivity)

            Service.nick(nick).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    if(response.isSuccessful){
                        binding.nickSubTv.text = "사용가능한 닉네임입니다."
                        binding.nickSubTv.setTextColor(Color.parseColor("#000000"))
                        binding.nickBoxIv.setImageResource(R.drawable.info_nick)
                    }else{
                        binding.nickSubTv.text = "중복된 닉네임입니다."
                        binding.nickSubTv.setTextColor(Color.parseColor("#E13017"))
                        binding.nickBoxIv.setImageResource(R.drawable.info_nick_error)
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e("NetworkError", t.message, t)
                    //웹통신이 실패했을 시
                    dialog.setTitle("통신 실패")
                    dialog.setMessage("통신에 실패하였습니다.")
                    dialog.show()

                }
            })
        }

        binding.backIv.setOnClickListener {
            finish()
        }
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }

    //갤러리에서 사진을 들고와 적용시키기
    fun lanuchActivityResult(intent: Intent){
        activityResult.launch(intent)
    }

    val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //결과 코드 OK , 결가값 null 아니면
        if(it.resultCode == RESULT_OK && it.data != null){
            //값 담기
            val uri  = it.data!!.data

            //화면에 보여주기
            Glide.with(this)
                .load(uri) //이미지
                .into(binding.profileIv) //보여줄 위치
        }
    }

    fun activityfinish(){
        finish()
    }
}