package com.youknow.yts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youknow.yts.data.local.entity.SummaryEntity
import com.youknow.yts.data.local.getRoomDatabase
import com.youknow.yts.data.local.source.SummaryDao
import com.youknow.yts.data.service.OpenAiService
import com.youknow.yts.service.whisper.WhisperService
import com.youknow.yts.service.ytdlp.YtDlp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val ytDlp: YtDlp = YtDlp(),
    private val whisperService: WhisperService = WhisperService(),
    private val openAiService: OpenAiService = OpenAiService(),
    private val summaryDao: SummaryDao = getRoomDatabase().getSummaryDao()
) : ViewModel() {

    init {
        viewModelScope.launch {
            val list = summaryDao.getAll()
            println("list: $list")
        }
    }

    var youtubeUrl: String by mutableStateOf("https://www.youtube.com/watch?v=ZYc13CKAN04")
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

        val sTime = System.currentTimeMillis()

        uiState = UiState.Processing(ProcessStep.DOWNLOAD_VIDEO)

        viewModelScope.launch {
            var summary = ytDlp.download(youtubeUrl)
            save(summary)

            uiState = UiState.Processing(ProcessStep.STT)
            val audioText = whisperService.translate()
            summary = summary.copy(fullText = audioText)
            save(summary)

            uiState = UiState.Processing(ProcessStep.SUMMARIZE)
            val result = openAiService.summarize(audioText)
            val processTime = System.currentTimeMillis() - sTime
            summary = summary.copy(summary = result, processTime = processTime)
            save(summary)

            uiState = UiState.Result(
                time = processTime,
                result = result
            )
        }
    }

    private fun save(summary: SummaryEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                summaryDao.insert(summary)
            }
        }
    }

}