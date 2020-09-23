package com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amebaownd.pikohan_niwatori.recordsoundandtext.repository.Repository
import com.amebaownd.pikohan_niwatori.recordsoundandtext.util.Event

class PermissionViewModel(private val repository: Repository) : ViewModel(){

    private var id ="null"
    private var _onClickedEvent = MutableLiveData<Event<Boolean>>(Event(false))
    val onClickedEvent : LiveData<Event<Boolean>> = _onClickedEvent
    fun start(id:String){
        this.id = id
    }

    fun onButtonClicked(){
        this._onClickedEvent.value = Event(true)
    }
}