package com.example.frontend

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.FragmentInfoPopupBinding

class InfoPopupFragment: Fragment() {

    lateinit var binding: FragmentInfoPopupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInfoPopupBinding.inflate(inflater, container, false)

        binding.btnIv.setOnClickListener {
            (activity as InfoActivity).changeFragment(this)
        }

        binding.closeIv.setOnClickListener {
            (activity as InfoActivity).changeFragment(this)
        }

        return binding.root
    }
}