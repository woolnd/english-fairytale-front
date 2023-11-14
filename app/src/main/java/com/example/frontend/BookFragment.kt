package com.example.frontend

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontend.databinding.FragmentBookBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookFragment: Fragment() {
    lateinit var binding: FragmentBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        var keywords = arrayListOf("tiger", "summer", "teaher", "friend", "school")

        binding.keywordRv.apply {
            adapter = KeywordBookAdapter().build(keywords)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor()) // Add your custom interceptor
            .build()


        var retrofit = Retrofit.Builder()
            .baseUrl("http://52.78.27.113:8080")//서버 주소를 적을 것
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var Service = retrofit.create(Service::class.java)


        Service.bookDetail(37).enqueue(object : Callback<BookDetailResponse>{
            var dialog = AlertDialog.Builder(context)
            override fun onResponse(call: Call<BookDetailResponse>, response: Response<BookDetailResponse>
            ) {
                var result = response.body()
                if(result != null){
                    binding.titleTv.text = result.title
                    binding.nickTv.text = "${result.memberName}의 이야기"
//                    var eng_text = result.engTale.toString()
//                    var kor_text = result.korTale.toString()
                    var eng_text = """"Once upon a time, in a small village nestled in a lush green valley, there lived a kind-hearted girl named Lily. Lily was known for her love and compassion towards all creatures, big and small. One sunny morning, as she roamed through the meadow, she stumbled upon a lost little puppy. The puppy had a sad look in its eyes and whimpered softly.\n\nLily's heart filled with empathy for the puppy, and she immediately picked it up in her gentle arms. She could sense that the poor puppy was in need of a loving home. With delight sparkling in her eyes, she made a promise to care for the puppy, whom she named Bella.\n\nTogether, Lily and Bella became inseparable. They spent their days exploring the enchanted forest, chasing butterflies, and playing near the babbling brook. Bella's tail wagged with an excitement that mirrored Lily's, and they cherished every moment of their new-found friendship.\n\nOne day, as the sun began to set, casting a golden glow over the village, they stumbled upon a peculiar trail of sparkling footprints leading deeper into the forest. Curiosity overcame them, and they decided to follow the trail to uncover the mystery behind it.\n\nDeeper into the dense forest they ventured, and they stumbled upon a hidden clearing. In the center of the clearing stood a majestic tree, its branches adorned with shimmering lights. Underneath the tree sat a wise old fairy named Luna. She had a gentle smile on her face as she beckoned Lily and Bella to approach.\n\nLuna explained that the magical tree was known as the \"Tree of Wishes.\" It was said that if one truly believed in their wish and whispered it to the tree, it would come true. Bella wagged her tail excitedly, sensing the enchantment surrounding them.\n\nLily closed her eyes, took a deep breath, and whispered her deepest desire to the tree. Suddenly, a soft breeze swept through the clearing, gathering the whispers of the forest and carrying them to the sky. As the breeze faded away, Lily opened her eyes and noticed a golden collar around Bella's neck, shimmering with magical energy.\n\nLuna explained that the collar was a symbol of Bella's bond with Lily, granting her eternal love, loyalty, and protection. From that day forward, Bella became not just a puppy, but a fairy companion to Lily, guiding her through life's challenges and bringing joy to all they encountered.\n\nWord of their adventures spread throughout the village, and soon, Lily and Bella became legendary figures, inspiring others with their unwavering bond and the power of love and friendship. Lily's acts of kindness and Bella's unwavering loyalty reminded everyone of the magic that exists in the world and the importance of cherishing the connections we share with others.\n\nAnd so, in this enchanting tale of love and magic, Lily and Bella's story became one that was told for generations, reminding people that sometimes, the greatest adventures are found in the embrace of a loving puppy and the bond forged with a kind-hearted soul.""""
                    var kor_text = """"옛날 어느 작은 마을은 푸른 계곡에 자리 잡고 있었습니다. 그 마을에 사은한 마음씨를 가진 소녀 릴리가 살고 있었습니다. 릴리는 크고 작은 모든 생명체에 대한 사랑과 동정을 가지고 알려져 있었습니다. 맑은 아침 햇빛이 비추는 어느 날, 릴리가 초원을 거닐다가 길 잃은 작은 강아지를 발견했습니다. 강아지는 슬픈 눈빛으로 살며시 울부짖었습니다.\n\n릴리는 강아지를 부드러운 팔로 안아주었습니다. 그는 가엾은 강아지가 사랑을 필요로 하고 있음을 느꼈습니다. 기뻐하는 눈빛으로, 릴리는 강아지를 돌보기로 다짐했습니다. 그리하여 강아지는 이름을 벨라라고 지었습니다.\n\n릴리와 벨라는 끊임없이 둘은 한 몸처럼 함께 였습니다. 아름다운 숲을 함께 탐험하며 나비를 쫓거나 맑은 시내 근처에서 놀았습니다. 벨라의 꼬리는 릴리의 흥분과 마찬가지로 흥겨움을 드러냈고, 그들은 그 새로운 사이를 시간을 통해 소중히 여겼습니다.\n\n어느 날, 해가 지면서 노을이 마을 위로 황금빛을 비추어 주었습니다. 그 때 그들은 미스터리를 파헤치기 위해 빛나는 발자국 흔적을 따라 숲 속으로 들어갔습니다. 조금씩 깊어지고 나니, 그들은 한 개인 영역을 맞게 되었습니다. 그 영역 가운데 서 있는 화려한 빛으로 장식된 웅장한 나무가 있었습니다. 나무 밑에는 달의 이름을 띈 현명한 나비가 앉아 있었습니다. 그녀는 릴리와 벨라가 다가갈 수 있도록 마주봅이며 부드러운 미소를 짓고 있었습니다.\n\n밧줄은 그 저주받은 나무가 소원의 나무로 알려져 있다는 점을 설명했습니다. 진정한 기원을 믿고 나무에 속삭여 말하면 이루어질 수 있다는 것이었습니다. 벨라는 기쁨으로 꼬리를 흔들었으며, 주변을 둘러싼 마법같은 분위기를 느꼈습니다.\n\n릴리는 눈을 감고 깊게 숨을 들이마시며 마음속으로 가장 깊은 소원을 나무에 속삭였습니다. 갑자기 살짝 바람이 분터져 숲의 속삭임을 바람이 하늘로 실어 갔습니다. 바람이 사라짐에 따라 릴리는 눈을 뜨고 벨라의 목에 빛나는 마법의 힘을 가진 황금 목걸이를 발견했습니다.\n\n달은 그 칼라가 릴리와 벨라의 유대감을 상징하는 것이라고 말했습니다. 이 목걸이는 사랑, 충성심 그리고 보호를 나타내는 영속적인 것이었습니다. 그 날부터 벨라는 단순히 강아지에 불과한 것이 아니라 릴리의 요정 동반자로서 그녀의 삶의 도전을 안내하고 마주치는 모든 이에게 기쁨을 안겨 주었습니다.\n\n그들의 모험에 대한 이야기는 사람들 사이로 빠르게 퍼지면서, 곧 둘은 전설적인 존재가 되었고 그들의 강한 유대관계와 사랑과 우정의 힘을 통해 모두를 영감을 주었습니다. 릴리의 친절과 벨라의 충성심은 모든 사람들에게 세상에 존재하는 마법 그리고 서로와의 연결의 중요성을 상기시켰습니다.\n\n그래서 이러한 사랑과 마법으로 가득 찬 이야기에서, 릴리와 벨라의 이야기는 세대에 걸쳐 전해지며, 때때로 가까운 동물로의 품과 따스한 마음의 연결이 창조되는 가장 큰 모험을 보여주었습니다.""""
//                    val eng_split = eng_text.split("\n")
//                    val kor_split = kor_text.split("\n")
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

                    binding.contentRv.apply {
                        adapter = ContentAdapter().build(itemList_eng_kor)
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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
        return binding.root
    }
}
