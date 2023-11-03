package com.example.frontend

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivitySearchBinding

class SearchActivity: AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    lateinit var tab1: SearchFragment
    lateinit var tab2: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tab1 = SearchFragment()
        tab2 = SearchFragment()

        supportFragmentManager.beginTransaction().add(R.id.search_fl, tab1).commit()

        binding.filterLeftIv.setOnClickListener {
            replaceView(tab1)
            binding.filterLeftIv.setImageResource(R.drawable.filter_left)
            binding.filterRightIv.setImageResource(R.drawable.filter_right_gray)
            binding.filterRightTv.setTextColor(Color.parseColor("#B5B6BD"))
            binding.filterLeftTv.setTextColor(Color.parseColor("#000000"))
        }

        binding.filterRightIv.setOnClickListener {
            replaceView(tab2)
            binding.filterLeftIv.setImageResource(R.drawable.filter_left_gray)
            binding.filterRightIv.setImageResource(R.drawable.filter_right)
            binding.filterRightTv.setTextColor(Color.parseColor("#000000"))
            binding.filterLeftTv.setTextColor(Color.parseColor("#B5B6BD"))
        }
    }

    private fun replaceView(tab: Fragment){
        var selectorFragment: Fragment? = null
        selectorFragment = tab
        selectorFragment?.let{
            supportFragmentManager.beginTransaction()
                .replace(R.id.search_fl, it).commit()
        }
    }
}