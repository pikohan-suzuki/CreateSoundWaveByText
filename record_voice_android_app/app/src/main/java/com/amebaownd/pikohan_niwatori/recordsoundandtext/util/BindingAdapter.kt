package com.amebaownd.pikohan_niwatori.recordsoundandtext.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:isVisible")
fun setAddEditItems(view: View, isVisible:Boolean?){
    if(isVisible!=null)
        view.visibility =
            if(isVisible)
                View.VISIBLE
            else
                View.GONE
}