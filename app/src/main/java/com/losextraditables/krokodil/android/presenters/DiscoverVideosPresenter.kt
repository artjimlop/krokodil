package com.losextraditables.krokodil.android.presenters

import com.losextraditables.krokodil.android.models.VideoModel
import com.losextraditables.krokodil.android.models.mapper.VideoModelMapper
import com.losextraditables.krokodil.android.views.NullVideosView
import com.losextraditables.krokodil.android.views.VideosView
import com.losextraditables.krokodil.core.actions.Action
import com.losextraditables.krokodil.core.actions.GetDiscoveredVideosAction
import com.losextraditables.krokodil.core.model.Video
import javax.inject.Inject

class DiscoverVideosPresenter @Inject constructor(private val getDiscoveredVideosAction
                              : GetDiscoveredVideosAction,
                              private val videoModelMapper: VideoModelMapper): Presenter {

    var view: VideosView? = null

    fun initialize(view: VideosView, firebaseEndpoint: String) {
        this.view = view
        loadDiscoveredVideos(firebaseEndpoint)
    }

    private fun loadDiscoveredVideos(firebaseEndpoint: String) {
        view?.showLoading()
        getDiscoveredVideosAction.getDiscoveredVideos(firebaseEndpoint,
                object : Action.Callback<List<Video>> {
            override fun onLoaded(videos: List<Video>) {
                showVideos(videoModelMapper.toModel(videos))
            }

            override fun onComplete() {

            }

            override fun onError() {
                showError()
            }
        })
    }

    private fun showError() {
        view?.hideLoading()
    }

    fun showVideos(videos: List<VideoModel>) {
        view?.hideLoading()
        view?.showVideos(videos)
    }
    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        this.view = NullVideosView()
    }

}