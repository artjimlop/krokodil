package com.losextraditables.krokodil.core.actions;

import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import com.losextraditables.krokodil.core.model.SearchItem;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class SearchVideosAction implements Action {

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;
  private final VideoRepository videoRepository;
  private Callback<List<SearchItem>> callback;
  private String query;

  @Inject public SearchVideosAction(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, VideoRepository videoRepository) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.videoRepository = videoRepository;
  }

  public void search(String query, Callback<List<SearchItem>> callback) {
    this.query = query;
    this.callback = callback;
    threadExecutor.execute(this);
  }

  @Override public void run() {
    try {
      if (videoRepository.search(query) != null) {
        notifyLoaded(videoRepository.search(query));
      } else {
        notifyLoaded(Collections.emptyList());
      }
    } catch (ServerCommunicationException error) {
      //TODO
    }
  }

  private void notifyLoaded(final List<SearchItem> videos) {
    this.postExecutionThread.post(() -> callback.onLoaded(videos));
  }
}
