package com.example.taskapp.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.SignUpFragmentBinding

class SignUpFragment: Fragment() {
    private lateinit var mainBinding:SignUpFragmentBinding
    private var emailPattern =Regex( "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private var phonePattern=Regex("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")
    private var formEmailValid=false
    private var formPhoneValid=false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainBinding= SignUpFragmentBinding.inflate(LayoutInflater.from(context))
        mainBinding.button2.isEnabled=false
        mainBinding.button2.setOnClickListener{
            val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)?.edit()
            preferences?.putString("email_key",mainBinding.emailEditText.text.toString())
            preferences?.putString("phone_key",mainBinding.phoneEditText.text.toString())
            preferences?.apply()
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,LogFragment()).commit()
        }
        mainBinding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(emailPattern)&&s.toString().isNotEmpty()) {
                        mainBinding.emailEditText.error="Invalid Email"
                        mainBinding.emailEditTextLayout.helperText="Please enter valid email id"
                        mainBinding.emailEditTextLayout.boxStrokeColor= Color.RED
                        formEmailValid = false
                    } else {
                        formEmailValid = true
                        mainBinding.emailEditTextLayout.helperText=""
                        mainBinding.emailEditTextLayout.boxStrokeColor= context?.let { ContextCompat.getColor(it,R.color.purple_500) }!!
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                mainBinding.button2.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })
        mainBinding.phoneEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(phonePattern)&& s.toString().isNotEmpty()) {
                        mainBinding.phoneEditText.error = "Invalid Number"
                        mainBinding.phoneEditTextLayout.helperText="Please enter valid phone number"
                        mainBinding.phoneEditTextLayout.boxStrokeColor= Color.RED
                        formPhoneValid = false
                    } else {
                        formPhoneValid = true
                        mainBinding.phoneEditTextLayout.helperText=""
                        mainBinding.phoneEditTextLayout.boxStrokeColor= context?.let { ContextCompat.getColor(it,R.color.purple_500) }!!
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                mainBinding.button2.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })

        return mainBinding.root
    }
}