package com.example.frontend

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivityBookBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class BookActivity: AppCompatActivity(),  TextToSpeech.OnInitListener{
    lateinit var binding: ActivityBookBinding
    private lateinit var tts: TextToSpeech
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tts = TextToSpeech(this, this)

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor()) // Add your custom interceptor
            .build()


        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.27.113:8080")//서버 주소를 적을 것
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var Service = retrofit.create(Service::class.java)

        Service.bookDetail(37).enqueue(object : Callback<BookDetailResponse> {
            var dialog = AlertDialog.Builder(this@BookActivity)
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>
            ) {
                var result = response.body()
                if(result != null){
                    var title = result.title
                    var nick = "${result.memberName}의 이야기"
                    var eng_text = result.engTale.toString()
                    var kor_text = result.korTale.toString()

                    val eng_remove = eng_text.replace("\\n", "").replace("\\n\\n", "").replace("\\", "").replace("\n", "")
                    val eng_split = eng_remove.split("(?<=\\.)".toRegex())

                    val kor_remove = kor_text.replace("\\n", "").replace("\\n\\n", "").replace("\\", "").replace("\n", "")
                    val kor_split = kor_remove.split("(?<=\\.)".toRegex())

                    val itemList_eng = ArrayList<String>()
                    val itemList_eng_kor = ArrayList<String>()

                    for(i in 0 until eng_split.size){
                        itemList_eng.add(eng_split[i])
                        itemList_eng_kor.add(eng_split[i])
                        itemList_eng_kor.add(kor_split[i])
                    }
                    val bundle = Bundle()
                    bundle.putString("title", title)
                    bundle.putString("nick", nick)
                    bundle.putStringArrayList("eng", itemList_eng)
                    bundle.putStringArrayList("kor", itemList_eng_kor)
                    val fragment_book  = BookFragment()
                    fragment_book.arguments = bundle
                    supportFragmentManager.beginTransaction().replace(R.id.main_fl, fragment_book).commit()

                    binding.english.setOnClickListener {
                        val currentImg = binding.english.drawable
                        val ttsOn = ContextCompat.getDrawable(this@BookActivity, R.drawable.book_english_after)
                        val ttsOff = ContextCompat.getDrawable(this@BookActivity, R.drawable.book_english)
                        if(currentImg != null && ttsOn != null && ttsOff != null){
                            if(areDrawablesEqual(currentImg, ttsOff)){
                                binding.english.setImageResource(R.drawable.book_english_after)
                                tts.speak(itemList_eng.toString(), TextToSpeech.QUEUE_FLUSH, null, "")

                            }
                            else if(areDrawablesEqual(currentImg, ttsOn)){
                                binding.english.setImageResource(R.drawable.book_english)
                                stopTTS()
                            }
                        }
                    }
                }
                else{
                    dialog.setTitle("통신 실패").setMessage("반환값이 없습니다.").show()
                }
            }

            override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                dialog.setTitle("통신 실패").setMessage("통신에 실패하였습니다.").show()

            }

        })
        binding.primary.setOnClickListener {
            val currentImg = binding.primary.drawable
            val translateOn = ContextCompat.getDrawable(this@BookActivity, R.drawable.book_primary_after)
            val translateOff = ContextCompat.getDrawable(this@BookActivity, R.drawable.book_primary)
            if(currentImg != null && translateOn != null && translateOff != null){
                if(areDrawablesEqual(currentImg, translateOff)){
                    binding.primary.setImageResource(R.drawable.book_primary_after)

                    val fragment = supportFragmentManager.findFragmentById(R.id.main_fl)
                    if (fragment is BookFragment) {
                        fragment.onPrimaryButtonOn()
                    }
                }
                else if(areDrawablesEqual(currentImg, translateOn)){
                    binding.primary.setImageResource(R.drawable.book_primary)

                    val fragment = supportFragmentManager.findFragmentById(R.id.main_fl)
                    if (fragment is BookFragment) {
                        fragment.onPrimaryButtonOff()
                    }
                }
            }
        }
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // 언어 설정 (한국어로 설정 예시)
            val result = tts.setLanguage(Locale.ENGLISH)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // 언어 데이터가 없거나 지원되지 않을 경우
                // 해당 언어를 사용할 수 없음
            }
        } else {
            // TTS 초기화 실패
        }
    }

    private fun stopTTS() {
        if (::tts.isInitialized) {
            // TTS를 중지하고 자원을 해제
            tts.stop()
            tts.shutdown()
        }
    }
    override fun onDestroy() {
        // 액티비티가 종료될 때 TTS 자원 해제
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}