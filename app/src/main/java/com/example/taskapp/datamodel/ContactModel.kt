package com.example.taskapp.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactModel(val id:String,val name:String,val phNo:String?,val email:String?) :
    Parcelable