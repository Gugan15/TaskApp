package com.example.taskapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.databinding.ContactsFragmentMainBinding
import com.example.taskapp.datamodel.ContactModel
import com.example.taskapp.ui.activities.DetailActivity
import com.example.taskapp.ui.adapters.ContactsListAdapter
import com.example.taskapp.ui.viewmodel.ContactMainStateEvent
import com.example.taskapp.ui.viewmodel.ContactViewModel
import com.example.taskapp.util.ContactState
import com.example.taskapp.util.ListClickListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.security.Permissions

@AndroidEntryPoint
class ContactsFragment:Fragment(),ListClickListener<ContactModel> {
    private val contactViewModel:ContactViewModel by viewModels()
    private lateinit var adapter:ContactsListAdapter
    private lateinit var contactsBinding: ContactsFragmentMainBinding
    private val permissionCode = 899
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        contactsBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.contacts_fragment_main, null, false) as ContactsFragmentMainBinding



        return contactsBinding.root
    }

    override fun onResume() {
        super.onResume()
        permissionCheck()
    }
    private fun initView() {
        contactsBinding.recyclerView2.layoutManager=LinearLayoutManager(context)
        adapter= context?.let { ContactsListAdapter(it,this) }!!
        contactsBinding.recyclerView2.adapter=adapter
        subscribeObservers()
    }

    private fun permissionCheck() {
        if (context?.applicationContext?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.READ_CONTACTS)
                } !=
                PackageManager.PERMISSION_GRANTED && context?.let {
                    ActivityCompat.checkSelfPermission(
                            it, Manifest.permission.READ_CONTACTS)
                } !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                    arrayOf(Manifest.permission.READ_CONTACTS), permissionCode)
            return
        } else {
            initView()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==permissionCode&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            initView()
        else
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    private fun subscribeObservers() {
        contactViewModel.setStateEvent(ContactMainStateEvent.ListEvent)
        contactViewModel.getDataState.observe(viewLifecycleOwner, { dataState ->
            when (dataState) {
                is ContactState.Success<List<ContactModel>> -> {
                    displayProgress(false)
                    loadContacts(dataState.data)
                }
                is ContactState.Loading -> {
                    displayProgress(true)
                }
                is ContactState.Error -> {
                    displayProgress(false)
                    displayError(dataState.exception.toString())
                }

            }
        })
    }

    private fun displayError(toString: String) {
        Snackbar.make(contactsBinding.root, "Some error occurred$toString",Snackbar.LENGTH_LONG).show()
    }

    private fun loadContacts(list:List<ContactModel>) {
        adapter.setContactList(list as ArrayList<ContactModel>)
        adapter.notifyDataSetChanged()

    }
    private fun displayProgress(b: Boolean) {
        if(b)
        {
            contactsBinding.progressBar2.visibility=View.VISIBLE
        }
        else{
            contactsBinding.progressBar2.visibility=View.GONE
        }
    }

    override fun onListClick(model: ContactModel) {
        startActivity(Intent(context,DetailActivity::class.java).putExtra("contact",model))
    }
}



