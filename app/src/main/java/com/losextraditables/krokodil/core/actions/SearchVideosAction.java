package com.losextraditables.krokodil.core.actions;

import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import com.losextraditables.krokodil.core.model.SearchItem;
import com.losextraditables.krokodil.core.model.Video;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;

public class SearchVideosAction implements Action {

  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;
  private final VideoRepository videoRepository;
  private Callback<List<Video>> callback;
  private String query;

  @Inject public SearchVideosAction(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, VideoRepository videoRepository) {
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
    this.videoRepository = videoRepository;
  }

  public void search(String query, Callback<List<Video>> callback) {
    this.query = query;
    this.callback = callback;
    threadExecutor.execute(this);
  }

  @Override public void run() {
    try {
      List<SearchItem> search = videoRepository.search(query);
      if (search != null) {
        List<String> ids = new ArrayList<>(search.size());
        for (SearchItem searchItem : search) {
          ids.add(searchItem.getVideoId());
        }
        notifyLoaded(videoRepository.getVideos(ids));
      } else {
        notifyLoaded(Collections.emptyList());
      }
    } catch (ServerCommunicationException error) {
      //TODO
    }
  }

  private void notifyLoaded(final List<Video> videos) {
    this.postExecutionThread.post(() -> callback.onLoaded(videos));
  }
}
