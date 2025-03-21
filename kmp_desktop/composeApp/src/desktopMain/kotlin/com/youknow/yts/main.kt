package com.youknow.yts

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.youknow.yts.service.ServiceManager

fun main() = application {

    val serviceManager = ServiceManager()

    Window(
        onCloseRequest = {
            serviceManager.kill()
            exitApplication()
        },
        title = "Youtube Summarizer",
    ) {
        App()
    }
}