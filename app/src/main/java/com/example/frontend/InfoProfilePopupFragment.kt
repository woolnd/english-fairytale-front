package com.example.frontend

import android.app.Activity
import android.content.Intent
import android.icu.text.IDNA.Info
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.frontend.databinding.FragmentInfoProfilePopupBinding

class InfoProfilePopupFragment: Fragment() {
    lateinit var binding: FragmentInfoProfilePopupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoProfilePopupBinding.inflate(inflater, container, false)

        binding.chooseCl.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            (activity as InfoActivity).lanuchActivityResult(intent)
            (activity as InfoActivity).changeFragment(this)
        }

        binding.deleteCl.setOnClickListener {
            (activity as InfoActivity).binding.profileIv.setImageResource(R.drawable.basic_profile)
            (activity as InfoActivity).changeFragment(this)
        }

        return binding.root
    }

}