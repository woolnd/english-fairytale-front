package com.example.frontend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.frontend.databinding.ActivityMakeBinding
import java.net.URI

class MakeActivity : AppCompatActivity() {

    val REQ_GALLERY = 1
    private lateinit var speechRecognizer: SpeechRecognizer

    lateinit var binding: ActivityMakeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()

        //RecognizerIntent 생성 (마이크)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName) //여분키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR") //언어설정

        binding.imageView.setOnClickListener {
            openGallery()
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.gray)

        binding.mikeIv.setOnClickListener {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@MakeActivity)
            speechRecognizer.setRecognitionListener(recognitionListener) //리스너 설정
            speechRecognizer.startListening(intent) //듣기 시작

        }


        binding.randomMakeBtnIv.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        }

        // ToKen list and print text
        val list = listOf(binding.token1Cl, binding.token2Cl, binding.token3Cl, binding.token4Cl,binding.token5Cl)
        val list2 = listOf(binding.token1,binding.token2,binding.token3,binding.token4,binding.token5)


        binding.addChipBoxIv.setOnClickListener {
            val text = binding.editText.text.toString()
            var text_count = 0
            for (i in text) text_count += 1

            if(text_count < 1 ) Toast.makeText(application,"다시 입력해주세요",Toast.LENGTH_SHORT).show()
            if(text_count > 15) Toast.makeText(application,"다시 입력해주세요",Toast.LENGTH_SHORT).show()

            else {
                binding.editText.setText(null)
                var check = 0

                for (i in 0..4) {
                    if (list[i].visibility == View.GONE) break
                    else check += 1
                }

                if (check < 5) {
                    list[check].visibility = View.VISIBLE
                    list2[check].text = text
                    binding.mainCreateBtn.setImageResource(R.drawable.make_book_btn)
                    binding.mainCreateBtn.setOnClickListener {
                        val intent = Intent(this, LoadingActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        }

        // if click close button, token close
        binding.close1.setOnClickListener {
            binding.token1Cl.visibility = View.GONE
            var check_button = 0
            for (i in 0..4) {
                if (list[i].visibility != View.GONE) check_button = 1
            }
            if (check_button == 0) binding.mainCreateBtn.setImageResource(R.drawable.main_create_button)
        }
        binding.close2.setOnClickListener {
            binding.token2Cl.visibility = View.GONE
            var check_button = 0
            for (i in 0..4) {
                if (list[i].visibility != View.GONE) check_button = 1
            }
            if (check_button == 0) binding.mainCreateBtn.setImageResource(R.drawable.main_create_button)
        }
        binding.close3.setOnClickListener {
            binding.token3Cl.visibility = View.GONE
            var check_button = 0
            for (i in 0..4) {
                if (list[i].visibility != View.GONE) check_button = 1
            }
            if (check_button == 0) binding.mainCreateBtn.setImageResource(R.drawable.main_create_button)
        }
        binding.close4.setOnClickListener {
            binding.token4Cl.visibility = View.GONE
            var check_button = 0
            for (i in 0..4) {
                if (list[i].visibility != View.GONE) check_button = 1
            }
            if (check_button == 0) binding.mainCreateBtn.setImageResource(R.drawable.main_create_button)
        }
        binding.close5.setOnClickListener {
            binding.token5Cl.visibility = View.GONE
            var check_button = 0
            for (i in 0..4) {
                if (list[i].visibility != View.GONE) check_button = 1
            }
            if (check_button == 0) binding.mainCreateBtn.setImageResource(R.drawable.main_create_button)
        }


        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var text = binding.editText.text.toString()
                if(verifyId(text)){
                    binding.addChipBoxIv.setImageResource(R.drawable.add_button_box_after)
                }
                else{
                    binding.addChipBoxIv.setImageResource(R.drawable.add_button_box)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.beforebuttonIv.setOnClickListener {
            finish()
        }



    }

    //권한설정
    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(this@MakeActivity, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@MakeActivity,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }
    }
    // 리스너 설정
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
        }
        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {}
        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}
        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}
        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
        }
        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
        }
        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
//            val list = listOf(binding.token1Cl, binding.token2Cl, binding.token3Cl, binding.token4Cl,binding.token5Cl)
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (matches != null) {
                val editable = Editable.Factory.getInstance().newEditable("")
                for (i in matches.indices) {
                    editable.append(matches[i])
                }
                binding.editText.text = editable
            }
        }
        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}
        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }


    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK) //선택할수있는 창이나온다 -> 어떤 종류의 데이터를 선택할지 정한다.
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent,REQ_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            when(requestCode) {
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.imageView.setImageURI(uri)
                        binding.phototext.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun verifyId(id: String) : Boolean {
        val regexId = """^[a-z]{1,15}$""".toRegex()
        return regexId.matches(id)
    }
}