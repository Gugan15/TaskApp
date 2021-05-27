package com.example.taskapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.taskapp.datamodel.PostDatabaseEntity

@Database(entities = [PostDatabaseEntity::class],version = 1)
abstract class PostDatabase: RoomDatabase() {
    abstract fun postDao(): PostDao
    companion object {
        const val DATABASE_NAME:String = "post_db"
    }
}