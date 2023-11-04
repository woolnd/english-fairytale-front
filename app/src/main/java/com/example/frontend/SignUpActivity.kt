package com.example.frontend

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Join
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.frontend.databinding.ActivitySignupBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cnt = 0
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor()) // Add your custom interceptor
            .build()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.27.113:8080")//서버 주소를 적을 것
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var Service = retrofit.create(Service::class.java)

        binding.emailEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.emailEt.text.toString()

                if(verifyEmail(email)){
                    binding.error1Tv.visibility = View.GONE
                    binding.emailBoxIv.setImageResource(R.drawable.reset_pw_box)
                    cnt++
                }
                else{
                    binding.error1Tv.visibility = View.VISIBLE
                    binding.emailBoxIv.setImageResource(R.drawable.reset_pw_box_error)
                }
            }
        })

        binding.passwordEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.passwordEt.text.toString()
                if(verifyPw(pw)){
                    binding.error2Tv.visibility = View.GONE
                    binding.passwordBoxIv.setImageResource(R.drawable.reset_pw_box)
                    cnt++
                }
                else{
                    binding.error2Tv.visibility = View.VISIBLE
                    binding.passwordBoxIv.setImageResource(R.drawable.reset_pw_box_error)
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
                    binding.passwordEt.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOffIv.setImageResource(R.drawable.eye_off)
                    binding.passwordEt.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.passwordCheckEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val pw = binding.passwordEt.text.toString()
                val pw_chk = binding.passwordCheckEt.text.toString()
                if(pw == pw_chk){
                    binding.error3Tv.visibility = View.GONE
                    binding.passwordcheckBoxIv.setImageResource(R.drawable.reset_pw_box)
                    cnt++
                }
                else{
                    binding.error3Tv.visibility = View.VISIBLE
                    binding.passwordcheckBoxIv.setImageResource(R.drawable.reset_pw_box_error)
                }
            }
        })

        binding.eyeOff1Iv.setOnClickListener {
            val currentImg = binding.eyeOff1Iv.drawable
            val eyeoff = ContextCompat.getDrawable(this, R.drawable.eye_off)
            val eyeon = ContextCompat.getDrawable(this, R.drawable.eye_on)
            if(currentImg != null && eyeoff != null && eyeon != null){
                if(areDrawablesEqual(currentImg, eyeoff)){
                    binding.eyeOff1Iv.setImageResource(R.drawable.eye_on)
                    binding.passwordCheckEt.inputType = InputType.TYPE_CLASS_TEXT
                }
                else if(areDrawablesEqual(currentImg, eyeon)){
                    binding.eyeOff1Iv.setImageResource(R.drawable.eye_off)
                    binding.passwordCheckEt.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }

        binding.check1Iv.setOnClickListener {

            var nick = binding.nicknameEt.text.toString()
            var dialog = AlertDialog.Builder(this@SignUpActivity)

            Service.nick(nick).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) { //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    if(response.isSuccessful){
                        binding.nickSubTv.text = "사용가능한 닉네임입니다."
                        binding.nicknameBoxIv.setImageResource(R.drawable.info_nick)
                        cnt++
                    }else{
                        binding.nickSubTv.text = "중복된 닉네임입니다."
                        binding.nickSubTv.setTextColor(Color.parseColor("#E13017"))
                        binding.nicknameBoxIv.setImageResource(R.drawable.info_nick_error)
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
        binding.phonenumberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                val input = editable.toString().replace("-", "")

                // 입력이 비어 있거나 원하는 형식을 초과하는지 확인
                if (input.isEmpty() || input.length > 10) {
                    return
                }

                // 전화번호를 서식화
                val formattedNumber = buildString {
                    for (i in input.indices) {
                        if (i in 3..5 || i in 6..9) {
                            if ((i==3) || (i==7)) append("-")
                        }
                        append(input[i])
                    }
                }

                // EditText에 서식화된 텍스트를 설정하고 커서를 끝으로 이동
                binding.phonenumberEt.removeTextChangedListener(this) // 무한 재귀를 방지하기 위해 리스너를 제거
                binding.phonenumberEt.setText(formattedNumber)
                binding.phonenumberEt.setSelection(formattedNumber.length) // 커서를 끝으로 이동
                binding.phonenumberEt.addTextChangedListener(this) // 리스너를 다시 추가
            }
        })

        binding.check2Iv.setOnClickListener {
            var dialog = AlertDialog.Builder(this@SignUpActivity)
            dialog.setTitle("성공")
            dialog.setMessage("인증번호를 발송하였습니다.")
            cnt++
            dialog.show()
        }

        binding.check3Iv.setOnClickListener {
            var dialog = AlertDialog.Builder(this@SignUpActivity)
            dialog.setTitle("성공")
            dialog.setMessage("인증되었습니다.")
            cnt++
            dialog.show()
        }

        binding.checkbuttonIv.setOnClickListener {
            val currentImg = binding.checkbuttonIv.drawable
            val chk = ContextCompat.getDrawable(this, R.drawable.join_chk)
            val unchk = ContextCompat.getDrawable(this, R.drawable.checkbutton)
            if(currentImg != null && chk != null && unchk != null){
                if(areDrawablesEqual(currentImg, chk)){
                    binding.checkbuttonIv.setImageResource(R.drawable.checkbutton)
                }
                else if(areDrawablesEqual(currentImg, unchk)){
                    binding.checkbuttonIv.setImageResource(R.drawable.join_chk)
                    cnt++
                }
            }
        }

        binding.minicheck1Iv.setOnClickListener {
            val currentImg = binding.minicheck1Iv.drawable
            val chk = ContextCompat.getDrawable(this, R.drawable.join_check)
            val unchk = ContextCompat.getDrawable(this, R.drawable.check)
            if(currentImg != null && chk != null && unchk != null){
                if(areDrawablesEqual(currentImg, chk)){
                    binding.minicheck1Iv.setImageResource(R.drawable.check)
                }
                else if(areDrawablesEqual(currentImg, unchk)){
                    binding.minicheck1Iv.setImageResource(R.drawable.join_check)
                    cnt++
                }
            }
        }

        binding.minicheck2Iv.setOnClickListener {
            val currentImg = binding.minicheck2Iv.drawable
            val chk = ContextCompat.getDrawable(this, R.drawable.join_check)
            val unchk = ContextCompat.getDrawable(this, R.drawable.check)
            if(currentImg != null && chk != null && unchk != null){
                if(areDrawablesEqual(currentImg, chk)){
                    binding.minicheck2Iv.setImageResource(R.drawable.check)
                    binding.addbuttonIv.setImageResource(R.drawable.addbutton)
                }
                else if(areDrawablesEqual(currentImg, unchk)){
                    binding.minicheck2Iv.setImageResource(R.drawable.join_check)
                    binding.addbuttonIv.setImageResource(R.drawable.info_btn)
                    cnt++
                }
            }
        }

        binding.addbuttonIv.setOnClickListener {
            var email = binding.emailEt.text.toString()
            var pw = binding.passwordCheckEt.text.toString()
            var nick = binding.nicknameEt.text.toString()
            var phone = binding.phonenumberEt.text.toString()

            val imageFile = File("경로/이미지파일.jpg") // 이미지 파일의 경로로 변경
            val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val joinRequest = JoinRequest("엄재웅",phone, nick, email, pw)// 생성한 JoinRequest 객체

            var dialog = AlertDialog.Builder(this@SignUpActivity)
            Service.join(joinRequest, imagePart).enqueue(object : Callback<JoinResponse> {
                override fun onResponse(
                    call: Call<JoinResponse>,
                    response: Response<JoinResponse>
                ) {
                    var result: JoinResponse? = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    if(result != null){
                        if(result.memnberId!= null){
                            finish()
                        }
                        else{
                            dialog.setTitle("회원가입 실패")
                            dialog.setMessage("회원가입에 실패하였습니다.")
                            dialog.show()
                        }
                    }
                    else{
                        dialog.setTitle("회원가입 실패")
                        dialog.setMessage("회원가입에 실패하였습니다.")
                        dialog.show()
                    }
                }

                override fun onFailure(call: Call<JoinResponse>, t: Throwable) {
                    dialog.setTitle("통신 실패")
                    dialog.setMessage("통신에 실패하였습니다.")
                    dialog.show()
                }


            })
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