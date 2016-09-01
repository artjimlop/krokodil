package com.losextraditables.krokodil.android.views;

import com.losextraditables.krokodil.android.models.SearchItemModel;
import java.util.List;

public interface SearchVideosView {
  void showVideos(List<SearchItemModel> videoModels);

  void hideKeyboard();
}
