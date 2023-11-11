package com.example.frontend

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
import java.lang.Exception
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var getResult: ActivityResultLauncher<Intent>
    private var imagePart: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

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
                        binding.nickSubTv.setTextColor(Color.parseColor("#000000"))
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
            var pw = binding.passwordEt.text.toString()
            var nick = binding.nicknameEt.text.toString()
            var phone = binding.phonenumberEt.text.toString()


            val joinRequest = MemberRegisterDto(phone, nick, email, pw)// 생성한 JoinRequest 객체

            var dialog = AlertDialog.Builder(this@SignUpActivity)

            Service.registerMember(joinRequest, null).enqueue(object : Callback<Int> {
                override fun onResponse(
                    call: Call<Int>,
                    response: Response<Int>
                ) {
                    var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                    finish()
//                    if(result != null){
//                        dialog.setTitle("성공")
//                        dialog.setMessage("${result}")
//                        dialog.show()
//                    }
//                    else{
//                        dialog.setTitle("회원가입 실패")
//                        dialog.setMessage("회원가입에 실패하였습니다.")
//                        dialog.show()
//                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    dialog.setTitle("통신 실패")
                    dialog.setMessage("${t}")
                    dialog.show()
                }


            })
        }

//        val fragment_profile = JoinProfilePopupFragment()

//        binding.settingIv.setOnClickListener {
////            supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_profile).commit()
////            window.statusBarColor = ContextCompat.getColor(this, R.color.translucent_gray)
////            val intent = Intent(Intent.ACTION_PICK)
////            intent.type = "image/*"
////            lanuchActivityResult(intent)
//        }

//        // 현재 기기에 설정된 쓰기 권한을 가져오기 위한 변수
//        var writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE )
//
//        // 현재 기기에 설정된 읽기 권한을 가져오기 위한 변수
//        var readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//
        // 읽기 권한과 쓰기 권한에 대해서 설정이 되어있지 않다면
//        binding.settingIv.setOnClickListener {
//            var state = Environment.getExternalStorageState()
//
//            // 갤러리를 열어서 파일을 선택할 수 있도록 합니다.
//            if (TextUtils.equals(state, Environment.MEDIA_MOUNTED)) {
//                val intent = Intent(Intent.ACTION_PICK)
//                intent.type = "image/*"
//                getResult.launch(intent)
//            }
//        }
//        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                val uri  = it.data!!.data
//                //화면에 보여주기
//                Glide.with(this)
//                    .load(uri) //이미지
//                    .into(binding.profileIv)
//                val filePath = getRealPathFromURI(it.data?.data!!)
//                val file = File(filePath)
//                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
//                imagePart = MultipartBody.Part.createFormData("photo", "photo", requestFile)
//            }
//        }

        binding.settingIv.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            lanuchActivityResult(intent)
        }

        binding.backbuttonIv.setOnClickListener {
            finish()
        }
    }

    fun lanuchActivityResult(intent: Intent){
        activityResult.launch(intent)
    }

    val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK) {
            var currentImageUri: Uri? = it.data?.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, currentImageUri)
                getResizedBitmap(bitmap,10)
                binding.profileIv.setImageBitmap(bitmap)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }else{
            Log.d("ActivityResult", "something wrong")
        }
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
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

    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }
}