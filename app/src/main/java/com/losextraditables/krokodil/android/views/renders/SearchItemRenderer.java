package com.losextraditables.krokodil.android.views.renders;

import android.support.annotation.NonNull;
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
import com.losextraditables.krokodil.android.models.VideoModel;
import com.losextraditables.krokodil.android.views.listeners.VideoClickListener;
import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;
import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class SearchItemRenderer extends Renderer<VideoModel> {

  @BindView(R.id.video_image) ImageView image;
  @BindView(R.id.video_author) TextView author;
  @BindView(R.id.video_more_info) TextView moreInfo;
  @BindView(R.id.video_info) View info;
  @BindView(R.id.video_title) TextView title;
  @BindView(R.id.video_frame) View durationFrame;
  @BindView(R.id.video_duration) TextView duration;
  private VideoClickListener videoClickListener;
  private String time;
  private String visits;

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
    visits = String.format(getContext().getString(R.string.format_visits),
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
    time = getStringDuration(minutes) + ":" + getStringDuration(seconds);
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

  public void setClickListener(VideoClickListener videoClickListener) {
    this.videoClickListener = videoClickListener;
  }

  @OnClick(R.id.video_layout) void onVideoClicked() {
    VideoModel video = getContent();
    videoClickListener.onClick(video.getThumbnails().get(2), time, video.getTitle(),
        video.getChannelTitle(), visits, video.getDescription(),
        video.getId());
  }

  @Override protected void setUpView(View rootView) {

  }

  @Override protected void hookListeners(View rootView) {

  }
}
