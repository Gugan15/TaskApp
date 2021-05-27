package com.example.taskapp.database

import com.example.taskapp.datamodel.PostDatabaseEntity

class RoomDBServiceImpl constructor(private val blogDao: PostDao): RoomDBService {
    override suspend fun getList(): List<PostDatabaseEntity>{
        return blogDao.getList()
    }
}