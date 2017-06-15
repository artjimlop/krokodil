package com.losextraditables.krokodil.core.actions

import com.losextraditables.krokodil.core.infrastructure.VideoRepository
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor
import com.losextraditables.krokodil.core.model.Video
import javax.inject.Inject

class GetVideosByIdsAction @Inject constructor(private val threadExecutor: ThreadExecutor,
                                               private val postExecutionThread: PostExecutionThread,
                                               private val videoRepository: VideoRepository): Action {
    private var callback: Action.Callback<List<Video>>? = null
    private var videoIds: List<String>? = null

    fun getVideos(videoIds: List<String>,
                            callback: Action.Callback<List<Video>>) {
        this.videoIds = videoIds
        this.callback = callback
        threadExecutor.execute(this)
    }

    override fun run() {
        val videos = videoRepository.getVideos(videoIds)
        notifyLoaded(videos)
    }

    private fun notifyLoaded(videos: List<Video>) {
        this.postExecutionThread.post { callback!!.onLoaded(videos) }
    }
}