package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.YoutubeMainModel
import retrofit2.http.GET
import retrofit2.http.Url

interface YoutubeInterface {
    @GET
    suspend fun getVideoList(@Url url:String):YoutubeMainModel
}