package com.youknow.yts.service.gpt.model.resp

import kotlinx.serialization.Serializable

@Serializable
data class RespGpt(
    val choices: List<Choices>
)

@Serializable
data class Choices(
    val message: Message
)

@Serializable
data class Message(
    val content: String
)