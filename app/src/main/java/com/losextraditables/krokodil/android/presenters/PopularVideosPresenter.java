package com.losextraditables.krokodil.android.presenters;

import com.losextraditables.krokodil.android.models.mapper.VideoModelMapper;
import com.losextraditables.krokodil.android.views.PopularVideosView;
import com.losextraditables.krokodil.core.actions.Action;
import com.losextraditables.krokodil.core.actions.GetPopularVideosAction;
import com.losextraditables.krokodil.core.model.Video;
import java.util.List;
import javax.inject.Inject;

public class PopularVideosPresenter implements Presenter {

  private final GetPopularVideosAction getPopularVideosAction;
  private final VideoModelMapper videoModelMapper;
  private PopularVideosView view;

  @Inject public PopularVideosPresenter(GetPopularVideosAction getPopularVideosAction,
      VideoModelMapper videoModelMapper) {
    this.getPopularVideosAction = getPopularVideosAction;
    this.videoModelMapper = videoModelMapper;
  }

  public void initialize(PopularVideosView view) {
    this.view = view;
    getPopularVideosAction.getPopularVideos(new Action.Callback<List<Video>>() {
      @Override public void onLoaded(List<Video> videos) {
        view.showVideos(videoModelMapper.toModel(videos));
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
