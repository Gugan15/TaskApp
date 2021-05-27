package com.example.taskapp

import android.content.Context
import androidx.room.Room
import com.example.taskapp.database.DatabaseMapper
import com.example.taskapp.database.PostDao
import com.example.taskapp.database.PostDatabase
import com.example.taskapp.repository.ListRepository
import com.example.taskapp.util.interfaces.PostRetrofitInterface
import com.example.taskapp.util.mappers.PostMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("post_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context, PostDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    @Provides
    @Named("Repos")
    fun provideListRepository(dao: PostDao, dbMapper: DatabaseMapper, mapper: PostMapper, retrofitService: PostRetrofitInterface): ListRepository {
        return ListRepository(dao,dbMapper,mapper,retrofitService)
    }
}