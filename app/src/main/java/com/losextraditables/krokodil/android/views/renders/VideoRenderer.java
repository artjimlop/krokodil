package com.losextraditables.krokodil.android.views.renders;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.tools.Downloader;
import com.losextraditables.krokodil.android.models.VideoModel;
import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;

public class VideoRenderer extends Renderer<VideoModel> {

  @BindView(R.id.video_image) ImageView image;
  @BindView(R.id.video_author) TextView author;
  @BindView(R.id.video_more_info) TextView moreInfo;
  @BindView(R.id.video_info) View info;
  @BindView(R.id.video_title) TextView title;

  //@BindView(R.id.video_duration) TextView duration;

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    View inflatedView = inflater.inflate(R.layout.item_video_search, parent, false);
    ButterKnife.bind(this, inflatedView);
    return inflatedView;
  }

  @Override public void render() {
    //TODO things to show video here
    VideoModel video = getContent();
    image.setVisibility(View.VISIBLE);
    Picasso.with(getContext())
        .load(video.getThumbnails().get(2))
        .placeholder(R.drawable.loading_video)
        .into(image);
    info.setVisibility(View.VISIBLE);
    //duration.setVisibility(View.VISIBLE);
    //duration.setText(video.getDuration());
    title.setVisibility(View.VISIBLE);
    title.setText(video.getTitle());
    author.setText(video.getDescription());
  }

  @OnClick(R.id.video_layout) void onVideoClicked() {
    VideoModel video = getContent();
    new AlertDialog.Builder(getContext()).setTitle(video.getTitle())
        .setMessage(R.string.download_song_description)
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          dialog.dismiss();
        })
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
          Downloader downloader = new Downloader();
          downloader.getYoutubeDownloadUrl("https://www.youtube.com/watch?v=" + video.getId(), getContext());
        })
        .show();
  }

  @Override protected void setUpView(View rootView) {

  }

  @Override protected void hookListeners(View rootView) {

  }
}
