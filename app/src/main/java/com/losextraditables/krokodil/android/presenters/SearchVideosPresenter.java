package com.losextraditables.krokodil.android.presenters;

import com.losextraditables.krokodil.android.models.mapper.SearchModelMapper;
import com.losextraditables.krokodil.android.models.mapper.VideoModelMapper;
import com.losextraditables.krokodil.android.views.SearchVideosView;
import com.losextraditables.krokodil.core.actions.Action;
import com.losextraditables.krokodil.core.actions.SearchVideosAction;
import com.losextraditables.krokodil.core.model.Video;
import java.util.List;
import javax.inject.Inject;

public class SearchVideosPresenter implements Presenter {

  private SearchVideosView view;
  private final SearchVideosAction searchVideosAction;
  private final VideoModelMapper videoModelMapper;
  private final SearchModelMapper searchModelMapper;

  @Inject public SearchVideosPresenter(SearchVideosAction searchVideosAction,
      VideoModelMapper videoModelMapper, SearchModelMapper searchModelMapper) {
    this.searchVideosAction = searchVideosAction;
    this.videoModelMapper = videoModelMapper;
    this.searchModelMapper = searchModelMapper;
  }

  public void initialize(SearchVideosView view) {
    this.view = view;
  }

  public void search(String query) {
    view.showLoading();
    searchVideosAction.search(query, new Action.Callback<List<Video>>() {
      @Override public void onLoaded(List<Video> videos) {
        view.hideLoading();
        view.showVideos(videoModelMapper.toModel(videos));
        view.hideKeyboard();
      }

      @Override public void onComplete() {

      }

      @Override public void onError() {

      }
    });
  }

  @Override public void resume() {

  }

  @Override public void pause() {

  }

  @Override public void destroy() {

  }
}
