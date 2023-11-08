package com.example.frontend

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.FragmentInfoProfilePopupBinding

class JoinProfilePopupFragment: Fragment() {
    lateinit var binding: FragmentInfoProfilePopupBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoProfilePopupBinding.inflate(inflater, container, false)

        binding.chooseCl.setOnClickListener {

            when {
                ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    (activity as SignUpActivity).lanuchActivityResult(intent)
                    (activity as SignUpActivity).changeFragment(this)
                }
            }

        }

        binding.deleteCl.setOnClickListener {
            (activity as SignUpActivity).binding.profileIv.setImageResource(R.drawable.basic_profile)
            (activity as SignUpActivity).changeFragment(this)
        }

        return binding.root
    }

}