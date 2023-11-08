package com.example.frontend

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivityPwModifyBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PwModifyActivity: AppCompatActivity() {

    lateinit var binding: ActivityPwModifyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPwModifyBinding.inflate(layoutInflater)
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
        val fragment_popup = PwModifyPopupFragment()

        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        binding.eyeOff1Iv.setOnClickListener {
            val currentImg = binding.eyeOff1Iv.drawable
            val eyeoff = ContextCompat.getDrawable(this, R.drawable.eye_off)
            val eyeon = ContextCompat.getDrawable(this, R.drawable.eye_on)
            if(currentImg != null && eyeoff != null && eyeon != null){
                if(areDrawablesEqual(currentImg, eyeoff)){
                    binding.eyeOff1Iv.setImageResource(R.drawable.eye_on)
                    binding.pw1Et.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOff1Iv.setImageResource(R.drawable.eye_off)
                    binding.pw1Et.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.eyeOff2Iv.setOnClickListener {
            val currentImg = binding.eyeOff2Iv.drawable
            val eyeoff = ContextCompat.getDrawable(this, R.drawable.eye_off)
            val eyeon = ContextCompat.getDrawable(this, R.drawable.eye_on)
            if(currentImg != null && eyeoff != null && eyeon != null){
                if(areDrawablesEqual(currentImg, eyeoff)){
                    binding.eyeOff2Iv.setImageResource(R.drawable.eye_on)
                    binding.pw2Et.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOff2Iv.setImageResource(R.drawable.eye_off)
                    binding.pw2Et.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.eyeOff3Iv.setOnClickListener {
            val currentImg = binding.eyeOff3Iv.drawable
            val eyeoff = ContextCompat.getDrawable(this, R.drawable.eye_off)
            val eyeon = ContextCompat.getDrawable(this, R.drawable.eye_on)
            if(currentImg != null && eyeoff != null && eyeon != null){
                if(areDrawablesEqual(currentImg, eyeoff)){
                    binding.eyeOff3Iv.setImageResource(R.drawable.eye_on)
                    binding.pw3Et.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOff3Iv.setImageResource(R.drawable.eye_off)
                    binding.pw3Et.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.pw2Et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.pw2Et.text.toString()
                if(verifyPw(pw)){
                    binding.error2Tv.visibility = View.GONE
                    binding.pw2Iv.setImageResource(R.drawable.reset_pw_box)
                }
                else{
                    binding.error2Tv.visibility = View.VISIBLE
                    binding.pw2Iv.setImageResource(R.drawable.reset_pw_box_error)
                    binding.btnIv.setImageResource(R.drawable.info_btn_gray)
                    binding.btnTv.setTextColor(Color.parseColor("#5C5D61"))
                }
            }
        })

        binding.pw3Et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.pw2Et.text.toString()
                val pw_chk = binding.pw3Et.text.toString()
                if(pw.equals(pw_chk)){
                    binding.error3Tv.visibility = View.GONE
                    binding.pw3Iv.setImageResource(R.drawable.reset_pw_box)
                    binding.btnIv.setImageResource(R.drawable.info_btn)
                    binding.btnTv.setTextColor(Color.parseColor("#FFFFFF"))
                }
                else{
                    binding.error3Tv.visibility = View.VISIBLE
                    binding.pw3Iv.setImageResource(R.drawable.reset_pw_box_error)
                    binding.btnIv.setImageResource(R.drawable.info_btn_gray)
                    binding.btnTv.setTextColor(Color.parseColor("#5C5D61"))
                }
            }

        })



        binding.btnIv.setOnClickListener {
            val currentImg = binding.btnIv.drawable
            val orignalPw = binding.pw1Et.text.toString()
            val newPw = binding.pw2Et.text.toString()
            val request = ModifyPwRequest(orignalPw, newPw)

            Service.modifyPw(memberId, request).enqueue(object : Callback<Unit> {
                var dialog = AlertDialog.Builder(this@PwModifyActivity)
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    val btnon = ContextCompat.getDrawable(this@PwModifyActivity, R.drawable.info_btn)
                    if(result != null){
                        if(currentImg != null && btnon != null){
                            if(areDrawablesEqual(currentImg, btnon)){
                                supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
                                window.statusBarColor = ContextCompat.getColor(this@PwModifyActivity, R.color.translucent_gray)
                            }
                        }
                    }
                    else{
                        dialog.setTitle("변경 실패")
                        dialog.setMessage("변경에 실패하였습니다.")
                        dialog.show()

                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
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

    //fragment에서 activity finish()
    fun finishActivity(){
        finish()
    }
    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }

    fun areDrawablesEqual(drawable1: Drawable, drawable2: Drawable): Boolean {
        val bitmap1 = drawableToBitmap(drawable1)
        val bitmap2 = drawableToBitmap(drawable2)
        return bitmap1.sameAs(bitmap2)
    }


    // Drawable을 Bitmap으로 변환하는 함수
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    fun verifyPw(pw: String): Boolean{
        val regexPw = """^(?=.*[a-zA-Z])(?=.*\d).{2,10}$""".toRegex()
        return regexPw.matches(pw)
    }
}