package com.losextraditables.krokodil.android.presenters

import com.losextraditables.krokodil.android.views.SongsView
import com.losextraditables.krokodil.core.actions.Action
import com.losextraditables.krokodil.core.actions.GetDiscoveredVideosAction
import com.losextraditables.krokodil.core.model.SongParameters
import javax.inject.Inject

class DiscoverVideosPresenter @Inject constructor(private val getDiscoveredVideosAction
                              : GetDiscoveredVideosAction
): Presenter {

    var view: SongsView? = null

    fun initialize(view: SongsView, firebaseEndpoint: String) {
        this.view = view
        loadDiscoveredVideos(firebaseEndpoint)
    }

    private fun loadDiscoveredVideos(firebaseEndpoint: String) {
        view?.showLoading()
        getDiscoveredVideosAction.getDiscoveredVideos(firebaseEndpoint,
                object : Action.Callback<List<SongParameters>> {
            override fun onLoaded(songParameters: List<SongParameters>) {
                showVideos(songParameters)
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

    fun showVideos(videos: List<SongParameters>) {
        view?.hideLoading()
        view?.showSongs(videos)
    }
    override fun resume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroy() {
        //TODO this.view = NullVideosView()
    }

}