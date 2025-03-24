package com.youknow.yts.ui

sealed interface UiState {
    data object Input : UiState

    data class Processing(val step: ProcessStep) : UiState

    data class Result(
        val time: Long,
        val result: String
    ) : UiState
}

enum class ProcessStep {
    DOWNLOAD_VIDEO, STT, SUMMARIZE
}
