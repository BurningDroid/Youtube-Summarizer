package com.youknow.yts.service

open class ProcessService {

    internal var process: Process? = null

    fun kill() {
        process?.destroy()
        process = null
    }
}