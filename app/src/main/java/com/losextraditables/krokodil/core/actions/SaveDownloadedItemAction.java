package com.losextraditables.krokodil.core.actions;

import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import com.losextraditables.krokodil.core.model.SongParameters;
import javax.inject.Inject;

public class SaveDownloadedItemAction implements Action {

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;
  private final VideoRepository videoRepository;
  private Callback<Void> callback;
  private SongParameters songParameters;
  private String endpoint;

  @Inject public SaveDownloadedItemAction(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, VideoRepository videoRepository) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.videoRepository = videoRepository;
  }

  public void saveDownloadedItem(String endpoint, SongParameters songParameters, Callback<Void> callback) {
    this.callback = callback;
    this.endpoint = endpoint;
    this.songParameters = songParameters;
    threadExecutor.execute(this);
  }

  @Override public void run() {
    try {
      videoRepository.saveDownloadedItem(endpoint, songParameters);
      notifyCompleted();
    } catch (ServerCommunicationException error) {
      //TODO
    }
  }

  private void notifyCompleted() {
    this.postExecutionThread.post(() -> callback.onComplete());
  }
}
