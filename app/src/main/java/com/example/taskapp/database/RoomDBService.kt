package com.example.taskapp.database

import androidx.lifecycle.LiveData
import com.example.taskapp.datamodel.PostDatabaseEntity

interface RoomDBService {
    suspend fun getList():List<PostDatabaseEntity>
}