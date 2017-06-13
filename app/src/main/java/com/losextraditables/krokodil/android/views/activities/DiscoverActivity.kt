package com.losextraditables.krokodil.android.views.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.losextraditables.krokodil.R
import com.losextraditables.krokodil.android.models.VideoModel
import com.losextraditables.krokodil.android.presenters.DiscoverVideosPresenter
import com.losextraditables.krokodil.android.views.VideosView
import com.losextraditables.krokodil.android.views.renders.VideoRenderer
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import kotlinx.android.synthetic.main.activity_discover.*
import javax.inject.Inject


class DiscoverActivity : BaseActivity(), VideosView {

    @Inject
    lateinit var presenter: DiscoverVideosPresenter

    private var rendererBuilder: RendererBuilder<VideoModel>? = null
    private var adapter: RVRendererAdapter<VideoModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        component.inject(this)
        setupVideoList()

        presenter.initialize(this, androidApplication.firebaseEndpoint)
    }

    private fun setupVideoList() {
        this.discovered_video_list?.layoutManager = LinearLayoutManager(this)
        val videoRenderer = VideoRenderer()
        videoRenderer.setClickListener { thumbnail, time, title, author, visits, description, url ->
            startActivity(VideoDetailActivity.getIntent(this, thumbnail, time, title, author, visits,
                    description, url))
            overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out)
        }
        rendererBuilder = RendererBuilder<VideoModel>().withPrototype(videoRenderer)
                .bind(VideoModel::class.java, VideoRenderer::class.java)
    }

    override fun showVideos(videoModels: MutableList<VideoModel>?) {
        val adapteeCollection = ListAdapteeCollection(videoModels)
        adapter = RVRendererAdapter(rendererBuilder, adapteeCollection)
        this.discovered_video_list!!.adapter = adapter
    }

    override fun showLoading() {
        this.loading_view?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        this.loading_view?.visibility = View.GONE
    }
}
