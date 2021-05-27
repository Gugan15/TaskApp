package com.example.taskapp.di

import com.example.taskapp.util.interfaces.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): PostRetrofit {
        return Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(PostRetrofit::class.java)
    }
    @Singleton
    @Provides
    fun provideImageRetrofit(gson: Gson): ImageRetrofit {
        return Retrofit.Builder().baseUrl("https://api.imgbb.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ImageRetrofit::class.java)
    }
    @Singleton
    @Provides
    fun provideYouTubeRetrofit(gson: Gson): YoutubeInterface {
        return Retrofit.Builder().baseUrl("https://www.googleapis.com/youtube/v3/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(YoutubeInterface::class.java)
    }

    @Singleton
    @Provides
    fun providePostRetrofitService(retrofit: PostRetrofit): PostRetrofitInterface {
        return PostRetrofitInterfaceImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideYoutubeInterfaceImpl(retrofit: YoutubeInterface): YoutubeRetrofitInterface {
        return YoutubeInterfaceImpl(retrofit)
    }


}