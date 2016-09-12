package com.losextraditables.krokodil.android.presenters;

import com.losextraditables.krokodil.core.actions.Action;
import com.losextraditables.krokodil.core.actions.SaveDownloadedItemAction;
import com.losextraditables.krokodil.core.model.SongParameters;
import javax.inject.Inject;

public class VideoDetailPresenter implements Presenter {
  private final SaveDownloadedItemAction saveDownloadedItemAction;

  @Inject public VideoDetailPresenter(SaveDownloadedItemAction saveDownloadedItemAction) {
    this.saveDownloadedItemAction = saveDownloadedItemAction;
  }

  @Override public void resume() {

  }

  @Override public void pause() {

  }

  @Override public void destroy() {

  }

  public void markAsDownloaded(String firebaseEndpoint, SongParameters songParameters) {
    saveDownloadedItemAction.saveDownloadedItem(firebaseEndpoint, songParameters, new Action.Callback<Void>() {
      @Override public void onLoaded(Void aVoid) {

      }

      @Override public void onComplete() {

      }

      @Override public void onError() {

      }
    });
  }
}
