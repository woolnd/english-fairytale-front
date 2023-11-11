package com.example.frontend

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.databinding.FragmentMainBinding
import java.security.Key

class MainFragment: Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        var memberId = 0
        arguments?.let {
            memberId = it.getInt("memberId")
        }

        binding.communityBtnIv.setOnClickListener {
            val intent = Intent(context, CommunityActivity::class.java)
            startActivity(intent)

        }

        val books = arrayListOf<Book>(
            Book("The Magic of Knowledge: Lial’s Journey into Fantasia", arrayListOf("teacher"), false, 1),
            Book("The Pendant of Friendship", arrayListOf("animal", "girl"), true, 2),
            Book("Timmy and Leo’s Enchanted Adventure", arrayListOf("play", "friend"), true, 3),
            Book("The Guardian of the Enchanted Forest", arrayListOf("forest", "animal"), true, 4),
            Book("The Enchanted Journey of Jack, Sunny, and Baba", arrayListOf("father", "guidance", "bravery"), true, 5),
            Book("The Majestic Tiger and the Eternal Summer", arrayListOf("summer", "tiger"), false, 1),
        )

        binding.bookRv.apply {
            adapter = MainAdapter().build(books)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        binding.makeBtnIv.setOnClickListener {
            val intent = Intent(context, MakeActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }
        return binding.root
    }


}