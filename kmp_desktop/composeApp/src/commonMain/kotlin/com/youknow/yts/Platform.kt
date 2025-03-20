package com.youknow.yts

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform