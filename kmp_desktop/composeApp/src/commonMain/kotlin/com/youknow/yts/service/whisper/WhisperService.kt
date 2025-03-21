package com.youknow.yts.service.whisper

import com.youknow.yts.service.ProcessService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class WhisperService: ProcessService() {

    suspend fun translate(): String {
        return withContext(Dispatchers.IO) {
            process = ProcessBuilder("whisper", "temp.webm", "--output_format", "txt", "--language", "Korean", "--task", "transcribe")
                .redirectErrorStream(true)
                .start()

            val result = StringBuilder()
            process?.inputStream?.bufferedReader()?.use { br ->
                val text = br.readText()
                println("[yts] translate - $text")
                result.append(text)
            }
            process?.waitFor()

            File("temp.txt").inputStream().bufferedReader().use { br ->
                br.readText()
            }
        }
    }
}