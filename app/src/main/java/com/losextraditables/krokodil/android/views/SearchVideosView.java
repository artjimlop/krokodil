package com.losextraditables.krokodil.android.views;

import com.losextraditables.krokodil.android.models.VideoModel;
import java.util.List;

public interface SearchVideosView {
  void showVideos(List<VideoModel> videoModels);

  void hideKeyboard();

  void showLoading();

  void hideLoading();
}
