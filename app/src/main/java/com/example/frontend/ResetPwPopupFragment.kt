package com.example.frontend

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.FragmentResetPwPopupBinding

class ResetPwPopupFragment: Fragment() {
    lateinit var binding: FragmentResetPwPopupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResetPwPopupBinding.inflate(inflater, container, false)

        binding.btnIv.setOnClickListener {
            val intent = Intent(context, InitialActivity::class.java)
            startActivity(intent)
        }

        binding.closeIv.setOnClickListener {
            (activity as ResetPwActivity).changeFragment(this)
        }

        return binding.root
    }
}