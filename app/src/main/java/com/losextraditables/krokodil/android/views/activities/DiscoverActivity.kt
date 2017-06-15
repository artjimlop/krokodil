package com.losextraditables.krokodil.android.views.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.losextraditables.krokodil.R
import com.losextraditables.krokodil.android.presenters.DiscoverVideosPresenter
import com.losextraditables.krokodil.android.views.SongsView
import com.losextraditables.krokodil.android.views.renders.SongRenderer
import com.losextraditables.krokodil.core.model.SongParameters
import com.pedrogomez.renderers.ListAdapteeCollection
import com.pedrogomez.renderers.RVRendererAdapter
import com.pedrogomez.renderers.RendererBuilder
import kotlinx.android.synthetic.main.activity_discover.*
import javax.inject.Inject


class DiscoverActivity : BaseActivity(), SongsView {

    @Inject
    lateinit var presenter: DiscoverVideosPresenter

    private var rendererBuilder: RendererBuilder<SongParameters>? = null
    private var adapter: RVRendererAdapter<SongParameters>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discover)
        component.inject(this)
        setupSongsList()

        presenter.initialize(this, androidApplication.firebaseEndpoint)
    }

    private fun setupSongsList() {
        this.discovered_video_list?.layoutManager = LinearLayoutManager(this)
        val songRenderer = SongRenderer()
        songRenderer.setClickListener { thumbnail, time, title, author, visits, description, url ->
            startActivity(VideoDetailActivity.getIntent(this, thumbnail, time, title, author, visits,
                    description, url))
            overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out)
        }
        rendererBuilder = RendererBuilder<SongParameters>().withPrototype(songRenderer)
                .bind(SongParameters::class.java, SongRenderer::class.java)
    }

    override fun showSongs(videoModels: MutableList<SongParameters>?) {
        val adapteeCollection = ListAdapteeCollection(videoModels)
        adapter = RVRendererAdapter(rendererBuilder, adapteeCollection)
        this.discovered_video_list?.adapter = adapter
    }

    override fun showLoading() {
        this.loading_view?.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        this.loading_view?.visibility = View.GONE
    }
}
