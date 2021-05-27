package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeThumbnail(
    @SerializedName("high")
    @Expose
    var videoImage:YoutubeThumbnailUrl
)