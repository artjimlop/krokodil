package com.losextraditables.krokodil.android.views;

import com.losextraditables.krokodil.android.models.VideoModel;
import java.util.List;

public interface VideosView {

  void showVideos(List<VideoModel> videoModels);

  void showLoading();

  void hideLoading();
}
