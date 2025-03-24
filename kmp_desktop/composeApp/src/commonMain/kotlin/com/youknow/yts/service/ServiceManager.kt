package com.youknow.yts.service

import com.youknow.yts.service.whisper.WhisperService
import com.youknow.yts.service.ytdlp.YtDlp

class ServiceManager(
    val ytDlp: ProcessService = YtDlp(),
    val whisperService: ProcessService = WhisperService()
) {

    fun kill() {
        ytDlp.kill()
        whisperService.kill()
        println("[yts] kill process")
    }
}