package com.example.frontend

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityLoginBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class LoginActivity: AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
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



        binding.emailEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.emailEt.text.toString()
                val pw = binding.pwEt.text.toString()

                if(verifyEmail(email)){
                    binding.error1Tv.visibility = View.GONE
                    binding.input1Iv.setImageResource(R.drawable.reset_pw_box)
                }
                else{
                    binding.error1Tv.visibility = View.VISIBLE
                    binding.input1Iv.setImageResource(R.drawable.reset_pw_box_error)
                    binding.btnIv.setImageResource(R.drawable.info_btn_gray)
                    binding.btnTv.setTextColor(Color.parseColor("#5C5D61"))
                }
            }
        })

        binding.pwEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.emailEt.text.toString()
                val pw = binding.pwEt.text.toString()
                if(verifyPw(pw) && verifyEmail(email)){
                    binding.error2Tv.visibility = View.GONE
                    binding.input2Iv.setImageResource(R.drawable.reset_pw_box)
                    binding.btnIv.setImageResource(R.drawable.info_btn)
                    binding.btnTv.setTextColor(Color.parseColor("#FFFFFF"))

                    binding.btnTv.setOnClickListener {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                else{
                    binding.error2Tv.visibility = View.VISIBLE
                    binding.input2Iv.setImageResource(R.drawable.reset_pw_box_error)
                    binding.btnIv.setImageResource(R.drawable.info_btn_gray)
                    binding.btnTv.setTextColor(Color.parseColor("#5C5D61"))
                }
            }

        })

        binding.eyeOffIv.setOnClickListener {
            val currentImg = binding.eyeOffIv.drawable
            val eyeoff = ContextCompat.getDrawable(this, R.drawable.eye_off)
            val eyeon = ContextCompat.getDrawable(this, R.drawable.eye_on)
            if(currentImg != null && eyeoff != null && eyeon != null){
                if(areDrawablesEqual(currentImg, eyeoff)){
                    binding.eyeOffIv.setImageResource(R.drawable.eye_on)
                    binding.pwEt.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOffIv.setImageResource(R.drawable.eye_off)
                    binding.pwEt.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.btnIv.setOnClickListener {
            var email = binding.emailEt.text.toString()
            val password = binding.pwEt.text.toString()
            var dialog = AlertDialog.Builder(this@LoginActivity)

            Service.login(LoginReqeust(email, password)).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    var result: LoginResponse? = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    if(result != null){
                        if(result.id != null){
                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("memberId",result.id)
                            startActivity(intent)
                        }
                        else{
                            dialog.setTitle("로그인 실패")
                            dialog.setMessage("로그인에 실패하였습니다.")
                            dialog.show()
                        }
                    }
                    else{
                        dialog.setTitle("로그인 실패")
                        dialog.setMessage("로그인에 실패하였습니다.")
                        dialog.show()
                    }

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
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

    fun verifyEmail(email: String): Boolean{
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        if(pattern.matcher(email).matches()){
            return true
        }
        else{
            return false
        }
    }

    fun verifyPw(pw: String): Boolean{
        val regexPw = """^(?=.*[a-zA-Z])(?=.*\d).{2,10}$""".toRegex()
        return regexPw.matches(pw)
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
}