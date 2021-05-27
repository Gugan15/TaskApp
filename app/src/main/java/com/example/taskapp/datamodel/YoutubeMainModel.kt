package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeMainModel(
    @SerializedName("items")
    @Expose
    var items:List<YoutubeEntity>
)
