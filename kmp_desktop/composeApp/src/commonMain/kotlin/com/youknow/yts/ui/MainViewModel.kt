package com.youknow.yts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youknow.yts.ytdlp.YtDlp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val ytDlp: YtDlp = YtDlp()
) : ViewModel() {

    var youtubeUrl: String by mutableStateOf("")
        private set

    var enableRunButton: Boolean by mutableStateOf(true)
        private set

    fun onInputUrl(url: String) {
        youtubeUrl = url
    }

    fun onClickRun() {
        if (youtubeUrl.isEmpty()) {
            return
        }

        enableRunButton = false

        viewModelScope.launch {
            ytDlp.download(youtubeUrl)

            enableRunButton = true
        }
    }

}