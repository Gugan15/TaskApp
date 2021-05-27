package com.example.taskapp.util


sealed class CurrentState<out R>  {
    data class Success<out T>(val data:T) :CurrentState<T>()
    data class Error(val exception: Exception):CurrentState<Nothing>()
    object Loading:CurrentState<Nothing>()
}