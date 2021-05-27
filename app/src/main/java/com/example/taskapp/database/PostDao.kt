package com.example.taskapp.database

import androidx.room.*
import com.example.taskapp.datamodel.PostDatabaseEntity

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PostDatabaseEntity):Long

    @Query("Select * from posts")
    suspend fun getList(): List<PostDatabaseEntity>
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(blog:PostDatabaseEntity):Int
    @Delete
    suspend fun deleteItem(blog:PostDatabaseEntity)
}