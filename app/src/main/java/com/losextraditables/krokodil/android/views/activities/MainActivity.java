package com.losextraditables.krokodil.android.views.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.clans.fab.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.tools.Downloader;
import com.losextraditables.krokodil.android.infrastructure.tools.Intents;
import com.losextraditables.krokodil.android.models.VideoModel;
import com.losextraditables.krokodil.android.presenters.PopularVideosPresenter;
import com.losextraditables.krokodil.android.views.PopularVideosView;
import com.losextraditables.krokodil.android.views.listeners.KrokodilPermissionListener;
import com.losextraditables.krokodil.android.views.renders.VideoRenderer;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.RendererBuilder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements PopularVideosView {

  @Inject PopularVideosPresenter presenter;

  @BindView(R.id.popular_video_list) RecyclerView popularVideos;
  @BindView(R.id.search_menu) FloatingActionButton menu;
  @BindView(R.id.content) ViewGroup rootView;
  @BindView(R.id.loading_view) ProgressBar loadingView;

  private RendererBuilder<VideoModel> rendererBuilder;
  private RVRendererAdapter<VideoModel> adapter;
  private PermissionListener storagePermissionListener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getComponent().inject(this);
    ButterKnife.bind(this);

    createPermissionListeners();

    popularVideos.setLayoutManager(new LinearLayoutManager(this));
    Context context = this;
    VideoRenderer videoRenderer = new VideoRenderer();
    videoRenderer.setClickListener((thumbnail, time, title, author, visits, description, url) -> {
      startActivity(VideoDetailActivity.getIntent(context, thumbnail, time, title, author, visits,
          description, url));
      overridePendingTransition(R.anim.detail_activity_fade_in, R.anim.detail_activity_fade_out);
    });
    rendererBuilder = new RendererBuilder<VideoModel>().withPrototype(videoRenderer)
        .bind(VideoModel.class, VideoRenderer.class);

    presenter.initialize(this);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_share, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      return true;
    } else if (item.getItemId() == R.id.menu_share) {
      shareApp();
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void showVideos(List<VideoModel> videoModels) {
    ListAdapteeCollection<VideoModel> adapteeCollection = new ListAdapteeCollection<>(videoModels);
    adapter = new RVRendererAdapter<>(rendererBuilder, adapteeCollection);
    popularVideos.setAdapter(adapter);
  }

  @Override public void showLoading() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override public void hideLoading() {
    loadingView.setVisibility(View.GONE);
  }

  @OnClick(R.id.search_menu) public void onSearchClick() {
    Intent intent = new Intent(this, SearchActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.menu_add_url) public void onDownloadViaLink() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(R.string.alert_youtube_link_title);

    // Set up the input
    final EditText input = new EditText(this);
    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
    input.setInputType(InputType.TYPE_CLASS_TEXT);
    builder.setView(input);

    // Set up the buttons
    builder.setPositiveButton(R.string.button_download_song, (dialog, which) -> {
      performDownload(input);
    });
    builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> {
      dialog.cancel();
    });

    builder.show();
  }

  private void performDownload(EditText input) {
    String url = input.getText().toString();
    Boolean hasDownloadedVideo = downloadVideoByLink(url);
    if (!hasDownloadedVideo) {
      showFeedback(getString(R.string.message_unknown_link));
    }
  }

  private void createPermissionListeners() {
    PermissionListener feedbackViewPermissionListener = new KrokodilPermissionListener(this);

    storagePermissionListener = new CompositePermissionListener(feedbackViewPermissionListener,
        SnackbarOnDeniedPermissionListener.Builder.with(rootView,
            R.string.storage_permission_first_explanation)
            .withOpenSettingsButton(R.string.button_settings)
            .build());
    Dexter.checkPermission(storagePermissionListener, Manifest.permission.WRITE_EXTERNAL_STORAGE);
  }

  public void showPermissionGranted(String permissionName) {
  }

  public void showPermissionDenied(String permissionName, boolean permanentlyDenied) {
    Snackbar.make(rootView, R.string.snackbar_permission_permanently_denied, Snackbar.LENGTH_LONG)
        .show();
  }

  public void showPermissionRationale(PermissionToken token) {
    new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
        .setMessage(R.string.write_permission_rationale_explanation)
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          dialog.dismiss();
          token.cancelPermissionRequest();
        })
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
          dialog.dismiss();
          token.continuePermissionRequest();
        })
        .setOnDismissListener(dialog -> token.cancelPermissionRequest())
        .show();
  }

  private void shareApp() {
    String subject = getString(R.string.share_app_subject);
    String message = getString(R.string.share_app_message);
    String url = getString(R.string.share_app_website);

    String sharedText = message + " " + url;

    Intent chooserIntent = ShareCompat.IntentBuilder.from(this)
        .setType("text/plain")
        .setSubject(subject)
        .setText(sharedText)
        .setChooserTitle(R.string.share_shot_chooser_title)
        .createChooserIntent();
    Intents.maybeStartActivity(this, chooserIntent);
  }

  private Boolean downloadVideoByLink(String url) {
    String youtubeBaseUrl = getString(R.string.youtube_link_pettern);
    Pattern youtubeLinkPattern = Pattern.compile(youtubeBaseUrl);
    Matcher youtubeLinkMatcher = youtubeLinkPattern.matcher(url);

    Pattern youtubeShortLinkPattern =
        Pattern.compile(getString(R.string.youtube_short_link_pettern));
    Matcher youtubeShortLinkMatcher = youtubeShortLinkPattern.matcher(url);

    if (youtubeLinkMatcher.find()) {
      Downloader downloader = new Downloader();
      downloader.getYoutubeDownloadUrl(url, this);
      return true;
    }

    if (youtubeShortLinkMatcher.find()) {
      String videoId = url.substring(youtubeShortLinkMatcher.end());
      Downloader downloader = new Downloader();
      downloader.getYoutubeDownloadUrl(youtubeBaseUrl + videoId, this);
      return true;
    }
    return false;
  }

  private void showFeedback(String message) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
  }
}
