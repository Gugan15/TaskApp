package com.example.taskapp.util

interface ViewType<out T> {
    fun data(): T
}