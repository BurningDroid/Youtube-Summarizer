package com.youknow.yts.service.ytdlp

import com.youknow.yts.data.local.entity.SummaryEntity
import com.youknow.yts.service.ProcessService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class YtDlp: ProcessService() {

    private val osName: String by lazy { System.getProperty("os.name") }
    private val execName: String by lazy {
        if (osName.contains("Windows", ignoreCase = true)) "yt-dlp.exe"
        else "yt-dlp_macos"
    }

    private val execFile: File by lazy {
        val inputStream: InputStream = this::class.java.getResourceAsStream("/$execName") ?: throw IllegalStateException("yt-dlp binary not found: $execName")
        val tempFile = File.createTempFile("yt-dlp", null)
        Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        tempFile.setExecutable(true) // 실행 권한 부여
        tempFile
    }

    suspend fun download(url: String): SummaryEntity {
        return withContext(Dispatchers.IO) {
            deleteTempFile()

            process = ProcessBuilder(execFile.absolutePath, "-o", "temp", "--get-title", "--get-thumbnail", url)
                .redirectErrorStream(true)
                .start()


            val result: List<String>? = process?.inputStream?.bufferedReader()?.use { br ->
                br.readLines()
            }
            process?.waitFor()
            SummaryEntity(
                id = url,
                title = result?.get(0) ?: "",
                thumbnailUrl = result?.get(1) ?: ""
            )
        }
    }

    private fun deleteTempFile() {
        val file = File("temp.webm")
        if (file.exists()) {
            file.delete()
        }
    }
}