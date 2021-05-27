package com.example.taskapp.database


import com.example.taskapp.datamodel.PostModel

interface RoomDataSource {

    suspend fun getList(): List<PostModel>
}