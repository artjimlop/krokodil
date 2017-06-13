package com.losextraditables.krokodil.core.model

data class SongParameters(val thumbnailUrl: String,
                          val duration: String,
                          val title: String,
                          val author: String,
                          val visits: String,
                          val description: String,
                          val videoId: String,
                          var timesDownloaded: Long) {
}
