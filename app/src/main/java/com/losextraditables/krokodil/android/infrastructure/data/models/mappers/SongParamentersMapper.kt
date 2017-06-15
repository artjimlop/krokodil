package com.losextraditables.krokodil.android.infrastructure.data.models.mappers

import com.losextraditables.krokodil.core.model.SongParameters
import com.losextraditables.krokodil.core.model.Video
import java.util.*
import javax.inject.Inject

class SongParamentersMapper @Inject constructor() {

    fun toModel(songParameters: SongParameters): Video {
        val video = Video(
                songParameters.videoId,
                songParameters.duration,
                Date(),
                songParameters.title,
                songParameters.description,
                mutableListOf(songParameters.thumbnailUrl),
                0L,
                songParameters.author
        )
        return video
    }

    fun toModel(bos: Collection<SongParameters>?): List<Video> {
        var models: List<Video>? = null
        if (bos != null && !bos.isEmpty()) {
            models = ArrayList<Video>(bos.size)
            for (bo in bos) {
                models.add(toModel(bo))
            }
        }
        return models!!
    }
}
