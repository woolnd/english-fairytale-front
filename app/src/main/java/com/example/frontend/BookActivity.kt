package com.example.frontend

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.ActivityBookBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder

class BookActivity : AppCompatActivity(), TextToSpeech.OnInitListener{
    lateinit var speechText: String
    lateinit var textToSpeech: TextToSpeech
    lateinit var binding: ActivityBookBinding
    var option_list = mutableListOf(0,0,0)
    var eng: String = ""
    var kor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment_popup = MakePopupFragment()

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
        val taleId = intent.getIntExtra("taleId", 0)

        Service.bookDetail(37).enqueue(object : Callback<BookDetailResponse> {
            var dialog = AlertDialog.Builder(this@BookActivity)
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>)
            {

                var result = response.body() //서버에서 받은 코드값을 duplic_code 객체에 넣음
                if (result != null) {
                    binding.nicknameTv.text = result.memberName
                    binding.bookTitleTv.text = result.title
                    binding.titleTv.text = result.title
//                    binding.nameTv.text = "${result.memberName}의 이야기"
                    binding.nameTv.text = "철수임의 이야기"
                    eng = result.engTale.toString()
                    kor = result.korTale.toString()
                    speechText = eng
                    initRecycler()

                } else {
                    dialog.setTitle("조회 실패")
                    dialog.setMessage("조회에 실패하였습니다.")
                    dialog.show()

                }
            }

            override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                dialog.setTitle("통신 실패")
                dialog.setMessage("통신에 실패하였습니다.")
                dialog.show()
            }
        })



//        binding.optionButton.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
//            window.statusBarColor = ContextCompat.getColor(this@BookActivity, R.color.translucent_gray)
//        }
//
//        binding.optionButton1.setOnClickListener {
//            supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
//            window.statusBarColor = ContextCompat.getColor(this@BookActivity, R.color.translucent_gray)
//        }

        binding.beforeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.gray)

        binding.mainFrame.addPanelSlideListener(PanelEventListener())

        binding.hearttitle.setOnClickListener {
            if (option_list[0] == 0) {
                option_list[0] = 1
                binding.heart.setImageResource(R.drawable.book_heart_after)
                var counting = binding.heartcount.text.toString().toInt()
                counting += 1
                binding.heartcount.setText(counting.toString())
            }
            else {
                option_list[0] = 0
                binding.heart.setImageResource(R.drawable.book_heart)
                var counting = binding.heartcount.text.toString().toInt()
                counting -= 1
                binding.heartcount.setText(counting.toString())
            }
        }
        binding.english.setOnClickListener {
            if (option_list[1] == 0) {
                option_list[1] = 1
                binding.english.setImageResource(R.drawable.book_english_after)
                val intent:Intent = Intent()
                intent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
                activityResult.launch(intent)
            }
            else {
                option_list[1] = 0
                binding.english.setImageResource(R.drawable.book_english)
                stopTextToSpeech()
            }
        }

        binding.beforeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    inner class PanelEventListener : SlidingUpPanelLayout.PanelSlideListener{
        override fun onPanelSlide(panel: View?, slideOffset: Float) {
        }

        override fun onPanelStateChanged(panel: View?, previousState: SlidingUpPanelLayout.PanelState?, newState: SlidingUpPanelLayout.PanelState?)
        {
            if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                window.statusBarColor = ContextCompat.getColor(this@BookActivity, R.color.gray)
                binding.headerCl.visibility = View.GONE
            } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                window.statusBarColor = ContextCompat.getColor(this@BookActivity, R.color.white)
                binding.headerCl.visibility = View.VISIBLE
            }
        }

    }


    private fun initRecycler() {
        val itemList_eng = ArrayList<String>()
        val itemList_eng_kor = ArrayList<String>()

        var textList_eng = mutableListOf<String>()
        var sentence_eng = StringBuilder()
        var textList_kor = mutableListOf<String>()
        var sentence_kor = StringBuilder()

        for (i in eng) {
            sentence_eng.append(i)
            if (i == '.') {
                sentence_eng.append("\n")
                textList_eng.add(sentence_eng.toString())
                sentence_eng = StringBuilder()
            }
        }

        for (i in kor) {
            sentence_kor.append(i)
            if (i == '.') {
                sentence_kor.append("\n")
                textList_kor.add(sentence_kor.toString())
                sentence_kor = StringBuilder()
            }
        }

        for(i in 0 until textList_eng.size){
            itemList_eng.add(textList_eng[i])
            itemList_eng_kor.add(textList_eng[i])
            itemList_eng_kor.add(textList_kor[i])
        }

        binding.sentenceRv.apply {
            adapter = SentenceAdapter().build(itemList_eng)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.primary.setOnClickListener {
            if (option_list[2] == 0) {
                option_list[2] = 1
                binding.primary.setImageResource(R.drawable.book_primary_after)
                binding.sentenceRv.apply {
                    adapter = SentenceAdapter().build(itemList_eng_kor)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }

            }
            else {
                option_list[2] = 0
                binding.primary.setImageResource(R.drawable.book_primary)
                binding.sentenceRv.apply {
                    adapter = SentenceAdapter().build(itemList_eng)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){

        //보이스가 있다면
        if(it.resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){

            //음성전환 준비
            textToSpeech = TextToSpeech(this, this)

        }else{ //없다면 다운로드
            //데이터 다운로드
            val installIntent: Intent = Intent()
            installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
            startActivity(installIntent)
        }
    }

    //TextToSpeech 엔진 초기화시 호출되는 함수
    override fun onInit(status: Int) {

        if(status == TextToSpeech.SUCCESS){

            //언어값
            val languageStatus: Int = textToSpeech.setLanguage(Locale.ENGLISH)

            //데이터 문제(데이터가 없거나 언어를 지원할 수 없다면)
            if(languageStatus == TextToSpeech.LANG_MISSING_DATA ||
                languageStatus == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this, "언어를 지원할 수 없습니다.",
                    Toast.LENGTH_SHORT).show()
            }else{ //데이터 성공
                //입력값 변수에 담기
                val data: String = speechText

                val speechRate = 0.5f  // 수정 가능
                textToSpeech.setSpeechRate(speechRate)

                var speechStatus: Int = 0

                //안드로이드 버전에 따른 조건(롤리팝보다 같거나 높다면
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH,
                        null, null)
                } else {
                    speechStatus = textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH,
                        null)
                }


                if(speechStatus == TextToSpeech.ERROR){
                    Toast.makeText(this, "음성전환 에러입니다.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }else{ //실패
            Toast.makeText(this, "음성전환 엔진 에러입니다.",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun stopTextToSpeech() {
        // TTS 중지
        if (::textToSpeech.isInitialized && textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }
}