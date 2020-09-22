package com.amebaownd.pikohan_niwatori.recordsoundandtext.util

import android.app.Activity
import androidx.fragment.app.Fragment

fun Fragment.getViewModelFactory(): ViewModelFactory {
    val repository = ServiceLoader.provideRepository()
    return ViewModelFactory(repository)
}

fun Activity.getViewModelFactory(): ViewModelFactory {
    val repository = ServiceLoader.provideRepository()
    return ViewModelFactory(repository)
}