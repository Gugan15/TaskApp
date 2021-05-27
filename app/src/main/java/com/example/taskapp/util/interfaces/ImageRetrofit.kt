package com.example.taskapp.util.interfaces

import okhttp3.MultipartBody
import okhttp3.ResponseBody

import retrofit2.http.*

interface ImageRetrofit {
    @Multipart
    @POST("/1/upload")
    suspend fun postImage(@Query("key")key:String,@Part image:MultipartBody.Part): ResponseBody
}