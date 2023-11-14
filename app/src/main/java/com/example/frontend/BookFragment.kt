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

        binding.titleTv.text = arguments?.getString("title")
        binding.nickTv.text = arguments?.getString("nick")


        binding.contentRv.apply {
            adapter = arguments?.getStringArrayList("eng")?.let { ContentAdapter().build(it) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    fun onPrimaryButtonOn() {
        binding.contentRv.apply {
            adapter = arguments?.getStringArrayList("kor")?.let { ContentAdapter().build(it) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    fun onPrimaryButtonOff() {
        binding.contentRv.apply {
            adapter = arguments?.getStringArrayList("eng")?.let { ContentAdapter().build(it) }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}
