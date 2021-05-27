package com.example.taskapp.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostEntity (
    @SerializedName("userId")
    @Expose
    var userId:Int,
    @SerializedName("id")
    @Expose
    var id:Int,
    @SerializedName("title")
    @Expose
    var title:String,
    @SerializedName("body")
    @Expose
    var body:String,
)