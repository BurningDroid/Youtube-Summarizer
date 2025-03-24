package com.youknow.yts.ui.nav

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object Summarize: Route

    @Serializable
    object Settings: Route

    @Serializable
    object History: Route

    @Serializable
    data class Profile(val id: String): Route
}
