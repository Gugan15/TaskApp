package com.example.taskapp.database

import com.example.taskapp.datamodel.PostModel

class RoomDataSourceImpl constructor(private val roomDBService: RoomDBService, private val mapper: DatabaseMapper):RoomDataSource {
    override suspend fun getList(): List<PostModel> {
        return mapper.mapFromEntityList(roomDBService.getList())!!
    }
}