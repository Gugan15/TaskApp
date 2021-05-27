package com.example.taskapp.di

import android.content.Context
import androidx.room.Room
import com.example.taskapp.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideBlogDB(@ApplicationContext context: Context): PostDatabase
    {
        return Room.databaseBuilder(context,PostDatabase::class.java,PostDatabase.DATABASE_NAME).fallbackToDestructiveMigration().build()
    }
    @Singleton
    @Provides
    fun provideBlogDao(blogDB: PostDatabase): PostDao
    {
        return blogDB.postDao()
    }
    @Singleton
    @Provides
    fun provideRoomDBService(dao: PostDao
    ): RoomDBService {
        return RoomDBServiceImpl(dao)
    }

    @Singleton
    @Provides
    fun provideRoomDataSource(
        roomDBService: RoomDBService,
        roomMapper: DatabaseMapper
    ): RoomDataSource {
        return RoomDataSourceImpl(roomDBService, roomMapper)
    }
}