package com.youknow.yts.ui.summarize

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.youknow.yts.data.local.entity.SummaryEntity
import com.youknow.yts.data.local.getRoomDatabase
import com.youknow.yts.data.local.source.SummaryDao
import com.youknow.yts.service.FileManager
import com.youknow.yts.service.gpt.OpenAiService
import com.youknow.yts.service.whisper.WhisperService
import com.youknow.yts.service.ytdlp.YtDlp
import com.youknow.yts.ui.ProcessStep
import com.youknow.yts.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SummarizeViewModel(
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

    var youtubeUrl: String by mutableStateOf("https://www.youtube.com/watch?v=qRsJC4eyBd4")
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
            println("[yts] summary: $summary")
            save(summary)

            uiState = UiState.Processing(ProcessStep.STT)
            val audioText = whisperService.transcribe()
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
            FileManager.clear()
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