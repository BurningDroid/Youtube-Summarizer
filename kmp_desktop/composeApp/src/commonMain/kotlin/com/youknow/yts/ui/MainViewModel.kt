package com.youknow.yts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var youtubeUrl: String by mutableStateOf("")
        private set

    fun onInputUrl(url: String) {
        youtubeUrl = url
    }
}