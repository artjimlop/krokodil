package com.losextraditables.krokodil.android.views;

import com.losextraditables.krokodil.core.model.SongParameters;

import java.util.List;

public interface SongsView {

  void showSongs(List<SongParameters> videoModels);

  void showLoading();

  void hideLoading();
}
