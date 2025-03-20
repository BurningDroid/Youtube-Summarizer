package com.youknow.yts.data.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class YtDlp {

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

    suspend fun download(url: String) {
        withContext(Dispatchers.IO) {
            deleteTempFile()

            val process = ProcessBuilder(execFile.absolutePath, "-o", "temp", url)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().use { br ->
                println(br.readText())
            }
            process.waitFor()
        }
    }

    private fun deleteTempFile() {
        val file = File("temp.webm")
        if (file.exists()) {
            file.delete()
        }
    }
}