package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.PostEntity

interface PostRetrofitInterface {
    suspend fun getPosts():List<PostEntity>
}