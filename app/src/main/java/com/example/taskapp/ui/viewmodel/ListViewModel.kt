package com.example.taskapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.repository.ListRepository
import com.example.taskapp.util.CurrentState
import com.example.taskapp.util.ViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) :ViewModel() {
    private val dataState:MutableLiveData<CurrentState<List<ViewType<*>>>> = MutableLiveData()
    val getDataState:LiveData<CurrentState<List<ViewType<*>>>> get() = dataState
    fun setStateEvent(mainStateEvent: MainStateEvent){
        //Kotlin coroutine
        viewModelScope.launch {

            when(mainStateEvent)
            {
                is MainStateEvent.ListEvent->{

                    repository.getList().onEach { data ->
                        dataState.value=data
                    }
                        .launchIn(viewModelScope)
                }
                is MainStateEvent.None->
                {
                    repository.getError().onEach {data->
                        dataState.value=data
                    }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

}
sealed class MainStateEvent{
    object ListEvent:MainStateEvent()
    object None:MainStateEvent()
}