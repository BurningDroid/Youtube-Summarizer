package com.youknow.yts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youknow.yts.data.service.OpenAiService
import com.youknow.yts.data.service.YtDlp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val ytDlp: YtDlp = YtDlp(),
    private val openAiService: OpenAiService = OpenAiService()
) : ViewModel() {

    var youtubeUrl: String by mutableStateOf("https://www.youtube.com/watch?v=ypVNTWsgBhU")
        private set

    var uiState: UiState by mutableStateOf(UiState.Input)
        private set

    fun onInputUrl(url: String) {
        youtubeUrl = url
    }

    fun onClickRun() {
        if (youtubeUrl.isEmpty()) {
            return
        }

        uiState = UiState.Processing(ProcessStep.DOWNLOAD_VIDEO)

        viewModelScope.launch {
            ytDlp.download(youtubeUrl)

            uiState = UiState.Processing(ProcessStep.STT)
            val audioText = openAiService.transcribeAudio()

            uiState = UiState.Processing(ProcessStep.SUMMARIZE)
            val result = openAiService.summarize(audioText)
            uiState = UiState.Result(result)
        }
    }

}