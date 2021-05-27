package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.PostEntity
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface PostRetrofit {
    @GET("/posts")
    suspend fun getPosts():List<PostEntity>
    @FormUrlEncoded
    @POST("/posts")
    suspend fun addPost(@Field("title") title:String,@Field("body") body:String,@Field("userId") userId:Int):PostEntity
}