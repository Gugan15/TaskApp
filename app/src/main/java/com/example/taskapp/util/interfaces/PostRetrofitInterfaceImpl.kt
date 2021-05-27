package com.example.taskapp.util.interfaces

import com.example.taskapp.datamodel.PostEntity

class PostRetrofitInterfaceImpl constructor(private val retrofit: PostRetrofit):PostRetrofitInterface {
    override suspend fun getPosts(): List<PostEntity> {
       return retrofit.getPosts()
    }

}