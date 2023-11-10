package com.example.frontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.FragmentMakePopupBinding
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MakePopupFragment: Fragment() {
    lateinit var binding: FragmentMakePopupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMakePopupBinding.inflate(inflater, container, false)

        binding.root.setOnTouchListener { _, _ -> true }

        val slidingUpPanelLayout = activity?.findViewById<SlidingUpPanelLayout>(R.id.main_frame)
        slidingUpPanelLayout?.setOnTouchListener { _, _ -> true }


        return binding.root
    }

}