package com.example.taskapp.util

import com.example.taskapp.datamodel.ContactModel

interface ContactInterface {
    suspend fun getContactsAsync():List<ContactModel>
}