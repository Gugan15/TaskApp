package com.example.taskapp.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostDatabaseEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="id")
    var id: Int,

    @ColumnInfo(name="userId")
    var userId: Int,
    @ColumnInfo(name="title")
    var title: String,
    @ColumnInfo(name="body")
    var body: String,


)