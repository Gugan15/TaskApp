package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.YoutubeEntity

interface YoutubeRetrofitInterface {
    suspend fun getVideoList():List<YoutubeEntity>
}