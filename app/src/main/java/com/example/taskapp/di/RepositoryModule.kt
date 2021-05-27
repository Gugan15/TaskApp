package com.example.taskapp.di

import com.example.taskapp.database.DatabaseMapper
import com.example.taskapp.database.PostDao
import com.example.taskapp.repository.ContactRepository
import com.example.taskapp.repository.ListRepository
import com.example.taskapp.repository.YoutubeRepository
import com.example.taskapp.util.ContactInterface
import com.example.taskapp.util.interfaces.PostRetrofitInterface
import com.example.taskapp.util.interfaces.YoutubeRetrofitInterface
import com.example.taskapp.util.mappers.PostMapper
import com.example.taskapp.util.mappers.YoutubeMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideListRepository(dao: PostDao,dbMapper: DatabaseMapper,mapper: PostMapper,retrofitService:PostRetrofitInterface):ListRepository{
        return ListRepository(dao,dbMapper,mapper,retrofitService)
    }
    @Singleton
    @Provides
    fun provideContactRepository(contact: ContactInterface):ContactRepository{
        return ContactRepository(contact)
    }
    @Singleton
    @Provides
    fun provideYoutubeRepository(mapper:YoutubeMapper,contact: YoutubeRetrofitInterface):YoutubeRepository{
        return YoutubeRepository(mapper,contact)
    }

}