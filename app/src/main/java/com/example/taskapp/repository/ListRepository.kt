package com.example.taskapp.repository

import android.view.View
import com.example.taskapp.database.DatabaseMapper
import com.example.taskapp.database.PostDao
import com.example.taskapp.datamodel.PostModel
import com.example.taskapp.datamodel.SampleModelViewType
import com.example.taskapp.util.CurrentState
import com.example.taskapp.util.ViewType
import com.example.taskapp.util.interfaces.PostRetrofitInterface
import com.example.taskapp.util.mappers.PostMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject
import kotlin.collections.ArrayList

class ListRepository @Inject constructor(private val dao: PostDao,private val dbMapper:DatabaseMapper,private val mapper: PostMapper,private val source:PostRetrofitInterface) {
    suspend fun getList(): Flow<CurrentState<ArrayList<ViewType<*>>>> = flow {
        emit(CurrentState.Loading)
        try {
            val cachedList=ArrayList<PostModel>()
            if(dao.getList().isNotEmpty()){
                cachedList.addAll(dbMapper.mapFromEntityList(dao.getList()))
            }
            else {
                for (i in source.getPosts())
                    dao.insert(dbMapper.mapToEntity(mapper.mapFromEntity(i)))
                cachedList.addAll(mapper.mapFromEntityList(source.getPosts()))
            }
            val list= ArrayList<ViewType<*>>()
            for(i in cachedList.indices) {
                list.add(SampleModelViewType(cachedList[i]))
            }
            emit(CurrentState.Success(list))
        } catch (e: Exception) {
            emit(CurrentState.Error(e))
        }
    }
suspend fun getError():Flow<CurrentState<ArrayList<ViewType<*>>>> = flow {
    emit(CurrentState.Error(Exception()))
}
}