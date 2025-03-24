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

    private fun getMetaInfo(url: String): SummaryEntity {
        process = ProcessBuilder(execFile.absolutePath, "--skip-download", "--print", "\"%(title)s%(thumbnail)s\"", url)
            .redirectErrorStream(true)
            .start()

        val result: List<String>? = process?.inputStream?.bufferedReader()?.use { br ->
            br.readLines()
        }

        process?.waitFor()
        println(result)

        val meta = result?.last()?.replace("\"", "")
        return SummaryEntity(
            id = url,
            title = meta?.substringBefore("https://") ?: "",
            thumbnailUrl = "https://${meta?.substringAfter("https://")}"
        )
    }

    suspend fun download(url: String): SummaryEntity {
        println("[yts] download: $url")
        return withContext(Dispatchers.IO) {
            val summary = getMetaInfo(url)
            deleteTempFile()

            process = ProcessBuilder(execFile.absolutePath, "-f", "bestaudio", "--extract-audio", "--audio-format", "wav", "-o", "temp", url)
                .redirectErrorStream(true)
                .start()

            val result: List<String>? = process?.inputStream?.bufferedReader()?.use { br ->
                br.readLines()
            }

            process?.waitFor()
            println(result)

            summary
        }
    }

    private fun deleteTempFile() {
        val file = File("temp.webm")
        if (file.exists()) {
            file.delete()
        }
    }
}