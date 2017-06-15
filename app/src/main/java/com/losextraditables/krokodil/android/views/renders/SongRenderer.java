package com.losextraditables.krokodil.android.views.renders;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.KMNumbers;
import com.losextraditables.krokodil.R;
import com.losextraditables.krokodil.android.views.listeners.VideoClickListener;
import com.losextraditables.krokodil.core.model.SongParameters;
import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongRenderer extends Renderer<SongParameters> {

  @BindView(R.id.video_image) ImageView image;
  @BindView(R.id.video_author) TextView author;
  @BindView(R.id.video_more_info) TextView moreInfo;
  @BindView(R.id.video_info) View info;
  @BindView(R.id.video_title) TextView title;
  @BindView(R.id.video_frame) View durationFrame;
  @BindView(R.id.video_duration) TextView duration;
  private String time;
  private String visits;
  private VideoClickListener videoClickListener;

  //@BindView(R.id.video_duration) TextView duration;

  @Override protected View inflate(LayoutInflater inflater, ViewGroup parent) {
    View inflatedView = inflater.inflate(R.layout.item_video_search, parent, false);
    ButterKnife.bind(this, inflatedView);
    return inflatedView;
  }

  @Override public void render() {
    SongParameters songParameters = getContent();
    image.setVisibility(View.VISIBLE);
    Picasso.with(getContext())
        .load(songParameters.getThumbnailUrl())
        .placeholder(R.drawable.loading_video)
        .into(image);
    info.setVisibility(View.VISIBLE);
    title.setVisibility(View.VISIBLE);
    title.setText(songParameters.getTitle());
    author.setText(songParameters.getAuthor());
    setupDuration(songParameters);
    setupVisits(songParameters);
  }

  private void setupVisits(SongParameters songParameters) {
    visits = String.format(getContext().getString(R.string.format_downloads),
        KMNumbers.formatNumbers(songParameters.getTimesDownloaded()));
    moreInfo.setVisibility(View.VISIBLE);
    moreInfo.setText(visits);
  }

  private void setupDuration(SongParameters songParameters) {
    durationFrame.setVisibility(View.VISIBLE);
    duration.setText(songParameters.getDuration());
  }

  @NonNull private String getStringDuration(int duration) {
    if (duration < 10) {
      return "0" + String.valueOf(duration);
    }
    return String.valueOf(duration);
  }

  public void setClickListener(VideoClickListener videoClickListener) {
    this.videoClickListener = videoClickListener;
  }

  @OnClick(R.id.video_layout) void onVideoClicked() {
    SongParameters songParameters = getContent();
    videoClickListener.onClick(songParameters.getThumbnailUrl(), time, songParameters.getTitle(),
        songParameters.getAuthor(), visits, songParameters.getDescription(),
        songParameters.getVideoId());
  }

  @Override protected void setUpView(View rootView) {

  }

  @Override protected void hookListeners(View rootView) {

  }
}
