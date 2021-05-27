package com.example.taskapp.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.MainContainerBinding
import com.example.taskapp.ui.fragments.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainContainerActivity:AppCompatActivity() {
    private lateinit var binding:MainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= MainContainerBinding.inflate(LayoutInflater.from(this),null,false)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.include.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        val fragment: Fragment?
        val c=intent.extras?.getString("ex")
        fragment = if(c=="Exchange") {
            ExchangeFragment()
        } else if (c=="Encrypt") {
            EncryptFragment()
        } else if(c=="Contacts"){
            ContactsFragment()
        }
        else{
            StoryMainFragment()
        }
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,fragment).commit()
    }
}