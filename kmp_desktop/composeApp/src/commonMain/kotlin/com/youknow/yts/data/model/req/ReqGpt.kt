package com.youknow.yts.data.model.req

import kotlinx.serialization.Serializable

@Serializable
data class ReqGpt(
    val model: String = "gpt-4o-mini",
    val messages: List<ReqMessage>,
)

@Serializable
data class ReqMessage(
    val role: String,
    val content: String
)