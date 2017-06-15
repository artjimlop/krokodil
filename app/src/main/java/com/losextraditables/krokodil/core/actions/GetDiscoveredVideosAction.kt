package com.losextraditables.krokodil.core.actions

import com.firebase.client.DataSnapshot
import com.firebase.client.FirebaseError
import com.firebase.client.GenericTypeIndicator
import com.firebase.client.ValueEventListener
import com.losextraditables.krokodil.core.actions.Action.Callback
import com.losextraditables.krokodil.core.infrastructure.VideoRepository
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor
import com.losextraditables.krokodil.core.model.SongParameters
import com.losextraditables.krokodil.core.model.Video
import java.util.*
import javax.inject.Inject

class GetDiscoveredVideosAction @Inject constructor(private val threadExecutor: ThreadExecutor,
                                                    private val postExecutionThread: PostExecutionThread,
                                                    private val videoRepository: VideoRepository,
                                                    private val getVideosByIdsAction: GetVideosByIdsAction): Action {
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
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val t = object : GenericTypeIndicator<List<SongParameters>>() {

                        }
                        val songParametersList = dataSnapshot.getValue(t)
                        val ids = ArrayList<String>(songParametersList!!.size)
                        songParametersList.mapTo(ids) { it.videoId }
                        getVideosByIdsAction.getVideos(ids,
                                object : Action.Callback<List<Video>> {
                                    override fun onLoaded(videos: List<Video>) {
                                        videosAreLoaded(videos)
                                    }

                                    override fun onComplete() {

                                    }

                                    override fun onError() {
                                    }
                                })
                    }

                    override fun onCancelled(firebaseError: FirebaseError) {
                        //TODO
                    }
                })
    }

    private fun videosAreLoaded(videos: List<Video>) {
        this.postExecutionThread.post { callback!!.onLoaded(videos) }
    }

    private fun notifyLoaded(videos: List<Video>) {
        this.postExecutionThread.post { callback!!.onLoaded(videos) }
    }
}