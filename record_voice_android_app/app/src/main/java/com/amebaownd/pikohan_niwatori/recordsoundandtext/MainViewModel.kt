package com.amebaownd.pikohan_niwatori.recordsoundandtext

import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {
    var id = ""
        private set

    fun setID(id:String){
        this.id = id
    }
}