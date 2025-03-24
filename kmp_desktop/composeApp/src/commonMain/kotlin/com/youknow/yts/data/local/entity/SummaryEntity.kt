package com.youknow.yts.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SummaryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val thumbnailUrl: String,
    val fullText: String = "",
    val summary: String = "",
    val datetime: Long = System.currentTimeMillis()
)