package com.losextraditables.krokodil.core.actions

import com.losextraditables.krokodil.core.actions.Action.Callback
import com.losextraditables.krokodil.core.infrastructure.VideoRepository
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor
import com.losextraditables.krokodil.core.model.Video
import java.util.*
import javax.inject.Inject

class GetDiscoveredVideosAction @Inject constructor(private val threadExecutor: ThreadExecutor,
                                                    private val postExecutionThread: PostExecutionThread,
                                                    private val videoRepository: VideoRepository): Action {
    private var callback: Callback<List<Video>>? = null
    private var endpoint: String? = null

    fun getDiscoveredVideos(endpoint: String,
            callback: Callback<List<Video>>) {
        this.endpoint = endpoint
        this.callback = callback
        threadExecutor.execute(this)
    }

    override fun run() {
        videoRepository.getDiscoveredVideos(endpoint,
                object: Callback<List<Video>> {
                    override fun onLoaded(result: List<Video>?) {
                        val ids = ArrayList<String>(result!!.size)
                        for (popularVideo in result) {
                            ids.add(popularVideo.id)
                        }
                        notifyLoaded(videoRepository.getVideos(ids))
                    }

                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }

    private fun notifyLoaded(videos: List<Video>) {
        this.postExecutionThread.post { callback!!.onLoaded(videos) }
    }
}