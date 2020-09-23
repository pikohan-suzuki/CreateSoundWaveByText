package com.amebaownd.pikohan_niwatori.recordsoundandtext.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amebaownd.pikohan_niwatori.recordsoundandtext.MainViewModel
import com.amebaownd.pikohan_niwatori.recordsoundandtext.repository.Repository
import com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.home.HomeViewModel
import com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.permission.PermissionViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val t = with(modelClass) {
            when {
                isAssignableFrom(HomeViewModel::class.java) ->
                    HomeViewModel(repository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel()
                isAssignableFrom(PermissionViewModel::class.java)->
                    PermissionViewModel(repository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModelClass $modelClass")
            }
        } as T
        return t
    }
}