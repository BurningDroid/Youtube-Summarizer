package com.youknow.yts.service.gpt

import com.youknow.yts.service.gpt.model.req.ReqGpt
import com.youknow.yts.service.gpt.model.req.ReqMessage
import com.youknow.yts.service.gpt.model.resp.RespGpt
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File

class OpenAiService(
    private val ioDispatchers: CoroutineDispatcher = Dispatchers.IO
) {

    private val apiKey: String by lazy { System.getenv("OPENAI_API_KEY") }

    private val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging)
            install(HttpTimeout) {
                requestTimeoutMillis = Long.MAX_VALUE
                connectTimeoutMillis = Long.MAX_VALUE
                socketTimeoutMillis = Long.MAX_VALUE
            }
        }
    }

    suspend fun summarize(rawText: String): String {
        if (rawText.isBlank()) {
            println("[yts] summarize - input is blank")
            val textFile = File("temp.txt")
            if (textFile.exists()) {
                textFile.delete()
            }
            return ""
        }

        return withContext(ioDispatchers) {
            try {
                val resp = client.post("${BASE_URL}chat/completions") {
                    contentType(ContentType.Application.Json)
                    header(HttpHeaders.Authorization, "Bearer $apiKey")
                    setBody(ReqGpt(messages = listOf(ReqMessage(role = "user", content = PROMPT_SUMMARIZE.format(rawText)))))
                }.body<RespGpt>()
                resp.choices.firstOrNull()?.message?.content ?: ""
            } catch (t: Throwable) {
                println("[yts] summarize - failed: ${t.message}")
                ""
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://api.openai.com/v1/"
        private const val PROMPT_SUMMARIZE = "다음은 영상에서 추출한 내용입니다. 핵심 내용 위주로 정리해주세요. 보기 좋은 포맷으로 작성해주세요: %s"
    }
}
