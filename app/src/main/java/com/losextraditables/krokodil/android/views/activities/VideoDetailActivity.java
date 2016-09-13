package com.losextraditables.krokodil.android.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.injector.component.ApplicationComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.component.DaggerVideosComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ActivityModule;
import com.losextraditables.krokodil.android.infrastructure.injector.module.VideoModule;
import com.losextraditables.krokodil.android.infrastructure.tools.Downloader;
import com.squareup.picasso.Picasso;

public class VideoDetailActivity extends BaseActivity {

  public static final String EXTRA_THUMBNAIL = "thumbnail";
  public static final String EXTRA_DURATION = "duration";
  public static final String EXTRA_TITLE = "title";
  public static final String EXTRA_AUTHOR = "author";
  public static final String EXTRA_VISITS = "visits";
  public static final String EXTRA_DESCRIPTION = "description";
  public static final String EXTRA_URL = "url";

  @BindView(R.id.video_image) ImageView thumbnail;
  @BindView(R.id.video_title) TextView titleView;
  @BindView(R.id.video_info) TextView infoView;
  @BindView(R.id.video_description) TextView descriptionView;
  @BindView(R.id.video_duration) TextView durationView;
  @BindView(R.id.download_button) TextView downloadView;
  @BindView(R.id.download_loading) ProgressBar loadingView;
  private String url;

  public static Intent getIntent(Context context, String thumbnail, String duration, String title,
      String author, String visits, String description, String url) {
    Intent intent = new Intent(context, VideoDetailActivity.class);
    intent.putExtra(EXTRA_THUMBNAIL, thumbnail);
    intent.putExtra(EXTRA_DURATION, duration);
    intent.putExtra(EXTRA_TITLE, title);
    intent.putExtra(EXTRA_AUTHOR, author);
    intent.putExtra(EXTRA_VISITS, visits);
    intent.putExtra(EXTRA_DESCRIPTION, description);
    intent.putExtra(EXTRA_URL, url);
    return intent;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_detail);
    ButterKnife.bind(this);
    if (getSupportActionBar() != null) {
      getActionBar().hide();
    }
    setupVideoInfo();
  }

  private void setupVideoInfo() {
    String thumbnailUrl = getIntent().getStringExtra(EXTRA_THUMBNAIL);
    String duration = getIntent().getStringExtra(EXTRA_DURATION);
    String title = getIntent().getStringExtra(EXTRA_TITLE);
    String author = getIntent().getStringExtra(EXTRA_AUTHOR);
    String visits = getIntent().getStringExtra(EXTRA_VISITS);
    String description = getIntent().getStringExtra(EXTRA_DESCRIPTION);
    url = getIntent().getStringExtra(EXTRA_URL);

    Picasso.with(this)
        .load(thumbnailUrl)
        .placeholder(R.drawable.loading_video)
        .into(thumbnail);
    durationView.setText(duration);
    titleView.setText(title);
    infoView.setText(getString(R.string.video_detail_info, author, visits));
    descriptionView.setText(description);
  }

  @Override protected void initializeInjector(ApplicationComponent applicationComponent) {
    applicationComponent.inject(this);
    DaggerVideosComponent.builder()
        .applicationComponent(applicationComponent)
        .activityModule(new ActivityModule(this))
        .videoModule(new VideoModule())
        .build()
        .inject(this);
  }

  @Override
  public void finish() {
    super.finish();
    overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
  }

  @OnClick(R.id.download_button) public void downloadClicked() {
    downloadView.setVisibility(View.GONE);
    loadingView.setVisibility(View.VISIBLE);
    Downloader downloader = new Downloader();
    downloader.getYoutubeDownloadUrl(url, this);
  }

  @OnClick(R.id.detail_background)
  public void onClickOutside() {
    finish();
  }
}
