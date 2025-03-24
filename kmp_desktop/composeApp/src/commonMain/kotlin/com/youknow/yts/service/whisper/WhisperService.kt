package com.youknow.yts.service.whisper

import com.youknow.yts.service.ProcessService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class WhisperService: ProcessService() {

    suspend fun transcribe(): String {
        return withContext(Dispatchers.IO) {
            val outputFile = File("temp.txt")
            if (outputFile.exists()) {
                outputFile.delete()
            }

            process = ProcessBuilder("whisper", "temp.wav", "--output_format", "txt", "--language", "Korean", "--task", "transcribe")
                .redirectErrorStream(true)
                .start()

            val result = StringBuilder()
            process?.inputStream?.bufferedReader()?.use { br ->
                val text = br.readText()
                println("[yts] transcribe - $text")
                result.append(text)
            }
            process?.waitFor()

            outputFile.createNewFile()
            outputFile.inputStream().bufferedReader().use { br ->
                br.readText()
            }
        }
    }
}