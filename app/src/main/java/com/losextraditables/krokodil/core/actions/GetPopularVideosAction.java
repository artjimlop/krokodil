package com.losextraditables.krokodil.core.actions;

import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import com.losextraditables.krokodil.core.model.Video;
import java.util.List;
import javax.inject.Inject;

public class GetPopularVideosAction implements Action {

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;
  private final VideoRepository videoRepository;
  private Callback<List<Video>> callback;

  @Inject public GetPopularVideosAction(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, VideoRepository videoRepository) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.videoRepository = videoRepository;
  }

  public void getPopularVideos(Callback<List<Video>> callback) {
    this.callback = callback;
    threadExecutor.execute(this);
  }

  @Override public void run() {
    try {
      notifyLoaded(videoRepository.getPopularVideos());
    } catch (ServerCommunicationException error) {
      //TODO
    }
  }

  private void notifyLoaded(final List<Video> videos) {
    this.postExecutionThread.post(() -> callback.onLoaded(videos));
  }
}
