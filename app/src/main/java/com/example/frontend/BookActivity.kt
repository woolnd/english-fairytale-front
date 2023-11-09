package com.example.frontend

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.accessibility.AccessibilityViewCommand.SetTextArguments
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.ActivityBookBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import java.util.Locale
import kotlin.text.StringBuilder

class BookActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    lateinit var binding: ActivityBookBinding
    lateinit var profileAdapter: ProfileAdapter
    val datas = mutableListOf<ProfileData>()

    lateinit var speechText: EditText
    lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dialog = AlertDialog.Builder(this@BookActivity)
        val title = intent.getStringExtra("title")

        dialog.setTitle("제발 성공").setMessage("${title}").show()
        //리사이클 뷰 생성
        initRecycler()


        binding.beforeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.gray)

        val option_list = mutableListOf(0,0,0)
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
            }
        }

        binding.primary.setOnClickListener {
            if (option_list[2] == 0) {
                option_list[2] = 1
                binding.primary.setImageResource(R.drawable.book_primary_after)

            }
            else {
                option_list[2] = 0
                binding.primary.setImageResource(R.drawable.book_primary)
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

        override fun onPanelStateChanged(
            panel: View?,
            previousState: SlidingUpPanelLayout.PanelState?,
            newState: SlidingUpPanelLayout.PanelState?
        ) {
        }

    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            //보이스가 있다면
            if(it.resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                //음성전환 준비
                textToSpeech = TextToSpeech(this, this)
            }else { //없으면 다운로드
                //데이터 다운로드
                val installIntent: Intent = Intent()
                installIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
                startActivity(installIntent)
            }

        }
        //TextToSpeech 엔진 초기화시 호출되는 함수
        override fun onInit(status: Int) {
            if(status == TextToSpeech.SUCCESS) {
                //언어값
                val languageStatus: Int = textToSpeech.setLanguage(Locale.KOREA)
                //데이터 문제 (데이터가 없거나 언어를 지원할 수 없다면)
                if(languageStatus == TextToSpeech.LANG_MISSING_DATA ||
                    languageStatus == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this,"언어를 지원할 수 없습니다.",Toast.LENGTH_SHORT).show()
                }else{//데이터 성공
                    //입력값 변수에 담기
                    val data: String = speechText.text.toString()
                    var speechStatus: Int = 0

                    //안드로이드 버전에 따른 조건 (롤리팝보다 같거나 높으면
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        speechStatus = textToSpeech.speak(data,TextToSpeech.QUEUE_FLUSH,null,null)
                    } else {
                        speechStatus = textToSpeech.speak(data,TextToSpeech.QUEUE_FLUSH,null)
                    }

                    if(speechStatus == TextToSpeech.ERROR) {
                        Toast.makeText(this,"음성전환 에러입니다..",Toast.LENGTH_SHORT).show()
                    }
                }
            }else { //실패
                Toast.makeText(this,"음성전환 엔진 에러입니다..",Toast.LENGTH_SHORT).show()
            }
        }


    fun put_engtext():String {
        var text_eng = """A dog is walking down the street, when he sees a bone in a trash can. “A bone! Yippee! How lucky for me!” he thinks. He grabs the bone, and happily runs home. He runs past the train station and the school. He runs through the park. He runs onto a bridge. On the bridge, the dog looks down into the deep, still water below. There, he sees another dog with a bone in its mouth. “Who is that dog?” he wonders. "What is he doing down there?" The dog stares at the other dog. The other dog stares back. “Where did that dog get such a BIG bone?” the dog wonders. "Why is his bone bigger than mine?" The dog growls at the other dog. The other dog growls too. “I want that big bone!” he thinks. The greedy dog decides to steal the other dog's bone. He leaps off the bridge and into the water. Splash! But as soon as he hits the water, the other dog disappears. There was never any other dog. It was just his own reflection! The water is very deep and the dog is surprised. “Woof woof woof! Help!” he barks. And when he barks, his bone drops from his mouth — and sinks to the bottom of the water. The dog swims to shore. He is wet and cold, and now he has no bone at all."""
        return text_eng
    }
    fun put_kortext():String {
        var text_kor = "개가 길을 걷다가 쓰레기통에 있는 뼈를 발견했습니다. \"뼈! 만세! 나한테는 정말 행운이었어!” 그는 생각. 그는 뼈를 잡고 행복하게 집으로 달려갑니다. 그는 기차역과 학교를 지나 달려갑니다. 그는 공원을 달리고 있다. 그는 다리 위로 달려갑니다. 다리 위에서 개는 깊고 잔잔한 물 아래를 내려다봅니다. 그곳에서 그는 입에 뼈가 물려 있는 또 다른 개를 본다. “저 개는 누구예요?” 그는 궁금해한다. \"그 사람은 거기서 뭘 하고 있는 거지?\" 그 개는 다른 개를 쳐다본다. 다른 개는 뒤를 돌아본다. \"저 개는 어디서 그렇게 큰 뼈를 얻었나요?\" 개는 궁금해합니다. \"왜 그 사람 뼈가 내 뼈보다 더 크죠?\" 그 개는 다른 개에게 으르렁거립니다. 다른 개도 으르렁거린다. “나도 그 큰 뼈를 원해요!” 그는 생각. 욕심 많은 개는 다른 개 뼈를 훔치기로 결심합니다. 그는 다리에서 뛰어내려 물 속으로 뛰어든다. 튀김! 그러나 그가 물에 닿자마자 다른 개는 사라져 버립니다. 다른 개는 없었습니다. 그것은 단지 그 자신의 반성이었습니다! 물이 너무 깊어서 개는 깜짝 놀랐어요. “우우우우우우! 돕다!\" 그는 짖는다. 그리고 그가 짖을 때, 그의 뼈가 그의 입에서 떨어져서 물 밑바닥으로 가라앉습니다. 개는 해안으로 헤엄쳐갑니다. 그는 젖고 추워서 이제 뼈가 전혀 없습니다."
        return text_kor
    }

    private fun initRecycler() {

        val rv_profile = binding.recycleRv

        val itemList = ArrayList<ProfileData>()

        //api 호출 아래는 테스트용
        var text_eng = put_engtext()
        var text_kor = put_kortext()

        var textList_eng = mutableListOf<String>()
        var sentence_eng = StringBuilder()
        var textList_kor = mutableListOf<String>()
        var sentence_kor = StringBuilder()

        for (i in text_eng) {
            sentence_eng.append(i)
            if ( i == '.') {
                sentence_eng.append("\n")
                textList_eng.add(sentence_eng.toString())
                sentence_eng = StringBuilder()
            }
        }

        for (i in text_kor) {
            sentence_kor.append(i)
            if(i== '.') {
                sentence_kor.append("\n")
                textList_kor.add(sentence_kor.toString())
                sentence_kor = StringBuilder()
            }
        }

        var texteng_length = 0
        for (i in text_eng) {
            texteng_length += 1
        }

        var textkor_length = 0
        for (i in text_kor) {
            textkor_length += 1
        }

        itemList.add(ProfileData(textList_kor[0],textList_eng[0]))
        itemList.add(ProfileData(textList_kor[1],textList_eng[1]))
        itemList.add(ProfileData(textList_kor[2],textList_eng[2]))

        val profileAdapter = ProfileAdapter(itemList)
        profileAdapter.notifyDataSetChanged()

//        rv_profile.adapter = profileAdapter
        rv_profile.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

    }
}