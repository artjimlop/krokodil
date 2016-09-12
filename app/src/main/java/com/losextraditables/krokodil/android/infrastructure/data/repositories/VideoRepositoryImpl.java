package com.losextraditables.krokodil.android.infrastructure.data.repositories;

import com.losextraditables.krokodil.android.infrastructure.data.models.mappers.SearchEntityMapper;
import com.losextraditables.krokodil.android.infrastructure.data.models.mappers.VideoEntityMapper;
import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.model.SearchItem;
import com.losextraditables.krokodil.core.model.SongParameters;
import com.losextraditables.krokodil.core.model.Video;
import java.util.List;
import javax.inject.Inject;

public class VideoRepositoryImpl implements VideoRepository {

  private final VideoDataSource videoDataSource;
  private final VideoEntityMapper videoEntityMapper;
  private final SearchEntityMapper searchEntityMapper;

  @Inject
  public VideoRepositoryImpl(VideoDataSource videoDataSource, VideoEntityMapper videoEntityMapper,
      SearchEntityMapper searchEntityMapper) {
    this.videoDataSource = videoDataSource;
    this.videoEntityMapper = videoEntityMapper;
    this.searchEntityMapper = searchEntityMapper;
  }

  @Override public List<Video> getPopularVideos() {
    return videoEntityMapper.toModel(videoDataSource.getPopulars());
  }

  @Override public List<SearchItem> search(String query) {
    return searchEntityMapper.toModel(videoDataSource.search(query));
  }

  @Override public List<Video> getVideos(List<String> videoIds) {
    return videoEntityMapper.toModel(videoDataSource.getVideosById(videoIds));
  }

  @Override public void saveDownloadedItem(String endpoint, SongParameters songParameters) {
    videoDataSource.saveDownloadedItem(endpoint, songParameters);
  }
}
