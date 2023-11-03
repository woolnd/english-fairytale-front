package com.example.frontend

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.frontend.databinding.ActivityResetPwBinding
import java.util.regex.Pattern

class ResetPwActivity: AppCompatActivity() {
    lateinit var binding: ActivityResetPwBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityResetPwBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val fragment_popup = ResetPwPopupFragment()

        binding.emailEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val email = binding.emailEt.text.toString()
                if(verifyEmail(email)){
                    binding.errorTv.visibility = View.GONE
                    binding.inputIv.setImageResource(R.drawable.reset_pw_box)
                    binding.btnIv.setImageResource(R.drawable.info_btn)
                    binding.btnTv.setTextColor(Color.parseColor("#FFFFFF"))

                    binding.btnIv.setOnClickListener {

                        supportFragmentManager.beginTransaction().replace(R.id.popup_fl, fragment_popup).commit()
                        window.statusBarColor = ContextCompat.getColor(this@ResetPwActivity, R.color.translucent_gray)
                    }
                }
                else{
                    binding.errorTv.visibility = View.VISIBLE
                    binding.inputIv.setImageResource(R.drawable.reset_pw_box_error)
                    binding.btnIv.setImageResource(R.drawable.info_btn_gray)
                    binding.btnTv.setTextColor(Color.parseColor("#5C5D61"))
                }
            }

        })

        binding.backIv.setOnClickListener {
            finish()
        }
    }

    fun verifyEmail(email: String): Boolean{
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        if(pattern.matcher(email).matches()){
            return true
        }
        else{
            return false
        }
    }

    fun changeFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .remove(fragment)
            .commit()
    }
}