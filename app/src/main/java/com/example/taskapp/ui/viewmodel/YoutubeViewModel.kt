package com.example.taskapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.datamodel.YoutubeModel
import com.example.taskapp.repository.YoutubeRepository
import com.example.taskapp.util.CurrentState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(private val repository: YoutubeRepository) : ViewModel() {
    private val dataState: MutableLiveData<CurrentState<List<YoutubeModel>>> = MutableLiveData()
    val getDataState: LiveData<CurrentState<List<YoutubeModel>>> get() = dataState
    fun setStateEvent(mainStateEvent: CurrentStateEvent){
        //Kotlin coroutine
        viewModelScope.launch {

            when(mainStateEvent)
            {
                is CurrentStateEvent.ListEvent->{

                    repository.getVideoList().onEach { data ->
                        dataState.value=data
                    }
                        .launchIn(viewModelScope)
                }
                is CurrentStateEvent.None->
                {
                    //Nothing happens

                }
            }
        }
    }

}
sealed class CurrentStateEvent{
    object ListEvent:CurrentStateEvent()
    object None:CurrentStateEvent()
}