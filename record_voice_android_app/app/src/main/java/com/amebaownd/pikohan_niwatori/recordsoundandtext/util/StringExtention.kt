package com.amebaownd.pikohan_niwatori.recordsoundandtext.ui.home

fun String?.isNotNullOrBlankOrEmpty(): Boolean {
    if (this != null)
        return this.isNotEmpty() && this.isNotBlank()
    return false
}