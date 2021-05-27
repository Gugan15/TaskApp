package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeThumbnailUrl(
    @SerializedName("url")
    @Expose
    var img:String
)