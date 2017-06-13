package com.losextraditables.krokodil.core.model

import java.util.*

data class Video(
        val id: String,
        val duration: String,
        val publishedAt: Date,
        val title: String,
        val description: String,
        val thumbnails: List<String>,
        val viewCount: Long,
        val channelTitle: String
)