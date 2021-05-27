package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class YoutubeSnippetModel(
@SerializedName("title")
@Expose
var title:String,
@SerializedName("description")
@Expose
var description:String,
@SerializedName("thumbnails")
@Expose
var thumbnails:YoutubeThumbnail
)