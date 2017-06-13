package com.losextraditables.krokodil.android.views

import com.losextraditables.krokodil.android.models.VideoModel

class NullVideosView:VideosView {
    override fun showVideos(videoModels: MutableList<VideoModel>?) {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

}
