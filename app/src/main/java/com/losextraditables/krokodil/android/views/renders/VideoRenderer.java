package com.losextraditables.krokodil.android.views.renders;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.KMNumbers;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.infrastructure.tools.Downloader;
import com.losextraditables.krokodil.android.models.VideoModel;
import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class VideoRenderer extends Renderer<VideoModel> {

  @BindView(R.id.video_image) ImageView image;
  @BindView(R.id.video_author) TextView author;
  @BindView(R.id.video_more_info) TextView moreInfo;
  @BindView(R.id.video_info) View info;
  @BindView(R.id.video_title) TextView title;
  @BindView(R.id.video_frame) View durationFrame;
  @BindView(R.id.video_duration) TextView duration;

  //@BindView(R.id.video_duration) TextView duration;

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    View inflatedView = inflater.inflate(R.layout.item_video_search, parent, false);
    ButterKnife.bind(this, inflatedView);
    return inflatedView;
  }

  @Override public void render() {
    VideoModel video = getContent();
    image.setVisibility(View.VISIBLE);
    Picasso.with(getContext())
        .load(video.getThumbnails().get(2))
        .placeholder(R.drawable.loading_video)
        .into(image);
    info.setVisibility(View.VISIBLE);
    title.setVisibility(View.VISIBLE);
    title.setText(video.getTitle());
    author.setText(video.getChannelTitle());
    setupDuration(video);
    setupVisits(video);
  }

  private void setupVisits(VideoModel video) {
    String visits = String.format(getContext().getString(R.string.format_visits),
        KMNumbers.formatNumbers(video.getViewCount()));
    moreInfo.setVisibility(View.VISIBLE);
    moreInfo.setText(visits);
  }

  private void setupDuration(VideoModel video) {
    durationFrame.setVisibility(View.VISIBLE);
    PeriodFormatter standard = ISOPeriodFormat.standard();
    Period period = standard.parsePeriod(video.getDuration());
    int hours = period.getHours();
    int minutes = period.getMinutes();
    int seconds = period.getSeconds();
    String time = getStringDuration(minutes) + ":" + getStringDuration(seconds);
    if (hours > 0) {
      time = getStringDuration(hours) + ":" + getStringDuration(minutes) + ":" + getStringDuration(
          seconds);
    }
    duration.setText(time);
  }

  @NonNull private String getStringDuration(int duration) {
    if (duration < 10) {
      return "0" + String.valueOf(duration);
    }
    return String.valueOf(duration);
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
          downloader.getYoutubeDownloadUrl("https://www.youtube.com/watch?v=" + video.getId(),
              getContext());
        })
        .show();
  }

  @Override protected void setUpView(View rootView) {

  }

  @Override protected void hookListeners(View rootView) {

  }
}
