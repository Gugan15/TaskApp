package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class YoutubeEntity (
    @SerializedName("id")
    @Expose
    var videoId:YoutubeIDModel,
    @SerializedName("snippet")
    @Expose
    var videoTitle:YoutubeSnippetModel,

)