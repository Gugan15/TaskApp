package com.example.taskapp.di

import android.content.Context
import com.example.taskapp.util.ContactInterface
import com.example.taskapp.util.ContactInterfaceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GetModule {
    @Singleton
    @Provides
    fun provideContactInterface(@ApplicationContext context: Context):ContactInterface{
        return ContactInterfaceImpl(context)
    }
}