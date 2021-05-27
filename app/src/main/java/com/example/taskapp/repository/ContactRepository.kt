package com.example.taskapp.repository

import com.example.taskapp.datamodel.ContactModel
import com.example.taskapp.util.ContactInterface
import com.example.taskapp.util.ContactState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class ContactRepository @Inject constructor( private val source: ContactInterface) {
    suspend fun getContacts(): Flow<ContactState<List<ContactModel>>> = flow {
        emit(ContactState.Loading)
        kotlinx.coroutines.delay(1000L)
        try {

            emit(ContactState.Success(source.getContactsAsync()))

        } catch (e: Exception) {
            emit(ContactState.Error(e))
        }
    }
}