package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.YoutubeEntity

class YoutubeInterfaceImpl constructor(private val retrofit: YoutubeInterface):YoutubeRetrofitInterface {
    override suspend fun getVideoList(): List<YoutubeEntity> {
        val list=retrofit.getVideoList("https://youtube.googleapis.com/youtube/v3/search?part=snippet&channelId=UC0FPjuZLQ16UpvLtbs6LYpg&maxResults=25&key=AIzaSyDIDZwOVBNJhbdpEOR6-T2L7qmjrt3EXo0")
        return list.items }
}