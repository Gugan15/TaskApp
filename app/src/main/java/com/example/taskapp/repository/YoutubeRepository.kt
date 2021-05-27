package com.example.taskapp.repository


import com.example.taskapp.datamodel.YoutubeModel
import com.example.taskapp.util.CurrentState
import com.example.taskapp.util.interfaces.YoutubeRetrofitInterface
import com.example.taskapp.util.mappers.YoutubeMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class YoutubeRepository @Inject constructor(private val mapper: YoutubeMapper, private val source: YoutubeRetrofitInterface) {
    suspend fun getVideoList(): Flow<CurrentState<List<YoutubeModel>>> = flow {
        emit(CurrentState.Loading)
        try {
            val finalList=source.getVideoList()
            emit(CurrentState.Success(mapper.mapFromEntityList(finalList)))
        } catch (e: Exception) {
            emit(CurrentState.Error(e))
        }
    }
}