package com.losextraditables.krokodil.core.actions

import com.losextraditables.krokodil.core.actions.Action.Callback
import com.losextraditables.krokodil.core.infrastructure.VideoRepository
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor
import com.losextraditables.krokodil.core.model.SongParameters
import javax.inject.Inject

class GetDiscoveredVideosAction @Inject constructor(private val threadExecutor: ThreadExecutor,
                                                    private val postExecutionThread: PostExecutionThread,
                                                    private val videoRepository: VideoRepository
): Action {
    private var callback: Callback<List<SongParameters>>? = null
    private var endpoint: String? = null

    fun getDiscoveredVideos(endpoint: String,
            callback: Callback<List<SongParameters>>) {
        this.endpoint = endpoint
        this.callback = callback
        threadExecutor.execute(this)
    }

    override fun run() {
        videoRepository.getDiscoveredVideos(endpoint,
                object : Action.Callback<List<SongParameters>> {
                    override fun onLoaded(result: List<SongParameters>?) {
                        notifyLoaded(result!!)
                    }

                    override fun onComplete() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onError() {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                })
    }

    private fun notifyLoaded(videos: List<SongParameters>) {
        this.postExecutionThread.post { callback!!.onLoaded(videos) }
    }
}