package com.example.taskapp.util

sealed class ContactState<out R>  {
    data class Success<out T>(val data:T):ContactState<T>()
    data class Error(val exception: Exception):ContactState<Nothing>()
    object Loading:ContactState<Nothing>()
}