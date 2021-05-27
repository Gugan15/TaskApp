package com.example.taskapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.datamodel.ContactModel
import com.example.taskapp.repository.ContactRepository
import com.example.taskapp.util.ContactState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ContactRepository) : ViewModel() {
    private val dataState: MutableLiveData<ContactState<List<ContactModel>>> = MutableLiveData()
    val getDataState: LiveData<ContactState<List<ContactModel>>> get() = dataState
    fun setStateEvent(mainStateEvent: ContactMainStateEvent){
        //Kotlin coroutine
        viewModelScope.launch {

            when(mainStateEvent)
            {
                is ContactMainStateEvent.ListEvent->{

                    repository.getContacts().onEach { data ->
                        dataState.value=data
                    }
                            .launchIn(viewModelScope)
                }
                is ContactMainStateEvent.None->
                {
                    //Nothing happens

                }
            }
        }
    }

}
sealed class ContactMainStateEvent{
    object ListEvent:ContactMainStateEvent()
    object None:ContactMainStateEvent()
}