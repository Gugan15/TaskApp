package com.example.taskapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import com.example.taskapp.R
import com.example.taskapp.databinding.LoginFragmentBinding
import com.example.taskapp.ui.activities.BottomNavigationActivity


class LogFragment: Fragment() {
    private lateinit var binding:LoginFragmentBinding
    var emailPattern =Regex( "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    var phonePattern=Regex("^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")
    private var formEmailValid=false
    private lateinit var viewGroup: ViewGroup
    private var formPhoneValid=false
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (container != null) {
            viewGroup=container
        }
        binding=LoginFragmentBinding.inflate(LayoutInflater.from(context),null,false)
        binding.imageView4.clipToOutline=true
        if(checkPref()==true) {
            BubbleShowCaseSequence().addShowCase(
                BubbleShowCaseBuilder(context as Activity).title("Enter Email here")
                    .targetView(binding.editTextTextPersonName).showOnce("12345")
            )
                .addShowCase(
                    BubbleShowCaseBuilder(context as Activity).title("Enter Mobile number here")
                        .backgroundColor(Color.RED).targetView(binding.editTextTextPersonName2).showOnce("123456")
                )
                .show()
        }
        else
        {
            BubbleShowCaseBuilder(context as Activity).title("First time? Register Here!").targetView(binding.textView15).showOnce("12").show()
            (context as Activity).getSharedPreferences("my_pref",Context.MODE_PRIVATE).edit().putBoolean("firstrun",true)?.apply()
        }
        binding.editTextTextPersonName.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }


            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(emailPattern)&&s.toString().isNotEmpty()) {
                     binding.editTextTextPersonName.endDrawableView.visibility=View.VISIBLE
                        binding.editTextTextPersonName.helperText.visibility=View.VISIBLE
                        binding.editTextTextPersonName.helperText.text = resources.getString(R.string.please_email)
                        formEmailValid = false
                    } else {
                        formEmailValid = true
                        binding.editTextTextPersonName.helperText.visibility=View.INVISIBLE
                        binding.editTextTextPersonName.endDrawableView.visibility=View.INVISIBLE
                    }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.button5.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })
        binding.editTextTextPersonName2.editText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    if (!s.matches(phonePattern)&& s.toString().isNotEmpty()) {
                        binding.editTextTextPersonName2.endDrawableView.visibility=View.VISIBLE
                        binding.editTextTextPersonName2.helperText.visibility=View.VISIBLE
                        binding.editTextTextPersonName2.helperText.text=resources.getString(R.string.please_phone)
                        formPhoneValid = false
                    } else {
                        binding.editTextTextPersonName2.endDrawableView.visibility=View.INVISIBLE
                        binding.editTextTextPersonName2.helperText.visibility=View.INVISIBLE
                        formPhoneValid = true
                          }

                }
            }

            override fun afterTextChanged(s: Editable?) {
                binding.button5.isEnabled = formEmailValid && formPhoneValid && s.toString().isNotEmpty()
            }
        })
        val animation=AnimationUtils.loadAnimation(context, R.anim.my_animation)
        val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        val emailPref=preferences?.getString("email_key","")
        val phonePref=preferences?.getString("phone_key","")
        binding.button5.setOnClickListener{
           if(binding.editTextTextPersonName.editText.text.toString()!=emailPref&&binding.editTextTextPersonName2.editText.text.toString()!=phonePref)
           {
               binding.editTextTextPersonName.startAnimation(animation)
               binding.editTextTextPersonName2.startAnimation(animation)
               showCustomDialog()
           }
            else if(binding.editTextTextPersonName.editText.text.toString()!=emailPref){
               binding.editTextTextPersonName.startAnimation(animation)
               showCustomDialog()
            }
            else if(binding.editTextTextPersonName2.editText.text.toString()!=phonePref){
               binding.editTextTextPersonName2.startAnimation(animation)
               showCustomDialog()
           }
            else {
               showCustomDialog()
           }
        }
        binding.textView15.setOnClickListener{
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container,SignUpFragment()).addToBackStack(null).commit()

        }
        return binding.root
    }
    private fun checkPref(): Boolean? {
        val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return preferences?.getBoolean("firstrun",false)
    }
     private fun validateCredentials():Boolean {
        val preferences=context?.getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        return binding.editTextTextPersonName.editText.text.toString() == preferences?.getString("email_key","") &&
                binding.editTextTextPersonName2.editText.text.toString() == preferences.getString("phone_key","")
    }
    private fun showCustomDialog() {
        val viewGroup: ViewGroup = viewGroup
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.custom_dialog, viewGroup, false)
        val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
        val lottie=dialogView.findViewById<LottieAnimationView>(R.id.lottie_animation)
        val statusText=dialogView.findViewById<TextView>(R.id.alertTextView)
        val okButton=dialogView.findViewById<TextView>(R.id.button7)
        val cancelButton=dialogView.findViewById<TextView>(R.id.button6)
        okButton.setOnClickListener{
            startActivity(Intent(context, BottomNavigationActivity::class.java))
        }

        if(validateCredentials()) {
            lottie.setAnimation(R.raw.success)
            lottie.playAnimation()
            statusText.text = resources.getString(R.string.login_success)
            statusText.setTextColor(Color.GREEN)
        }
        else {
            lottie.setAnimation(R.raw.error)
            lottie.playAnimation()
            statusText.text = resources.getString(R.string.login_failed)
            statusText.setTextColor(Color.RED)
        }
        builder?.setView(dialogView)
        val alertDialog: AlertDialog? = builder?.create()
        alertDialog?.show()
        cancelButton.setOnClickListener{
        alertDialog?.hide()
        }
    }

}
