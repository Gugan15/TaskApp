package com.example.taskapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.taskapp.R
import com.example.taskapp.databinding.ContactDetailScreenBinding
import com.example.taskapp.datamodel.ContactModel

class ContactDetailFragment:Fragment() {
    private lateinit var binding:ContactDetailScreenBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding=DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.contact_detail_screen,null,false) as ContactDetailScreenBinding
        val activity=activity as AppCompatActivity
        val model=activity.intent.getParcelableExtra<ContactModel>("contact")!!
        populateEditText(model)
        activity.setSupportActionBar(binding.contactDetailScreenInclude.toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.contactDetailScreenInclude.toolbar.setNavigationOnClickListener{
            activity.onBackPressed()
        }

        return binding.root
    }

    private fun populateEditText(model: ContactModel) {
        val str=model.name.split(" ")
        binding.editTextFirstName.setText(str[0])
        if(str.size>1)
            binding.editTextLastName.setText(str[1])
        binding.editTextPhone.setText(model.phNo)
        binding.editTextEmail.setText(model.email)
    }
}