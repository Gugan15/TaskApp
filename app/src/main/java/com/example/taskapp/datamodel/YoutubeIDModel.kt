package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeIDModel(
    @SerializedName("videoId")
    @Expose
    var id:String
)