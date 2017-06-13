package com.losextraditables.krokodil.android.infrastructure.data.repositories;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.krokodil.android.infrastructure.data.models.mappers.SearchEntityMapper;
import com.losextraditables.krokodil.android.infrastructure.data.models.mappers.SongParamentersMapper;
import com.losextraditables.krokodil.android.infrastructure.data.models.mappers.VideoEntityMapper;
import com.losextraditables.krokodil.core.actions.Action;
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
  private final SongParamentersMapper songParamentersMapper;

  @Inject
  public VideoRepositoryImpl(VideoDataSource videoDataSource, VideoEntityMapper videoEntityMapper,
                             SearchEntityMapper searchEntityMapper, SongParamentersMapper songParamentersMapper) {
    this.videoDataSource = videoDataSource;
    this.videoEntityMapper = videoEntityMapper;
    this.searchEntityMapper = searchEntityMapper;
    this.songParamentersMapper = songParamentersMapper;
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

  @Override public void getDiscoveredVideos(String endpoint,
                                         Action.Callback<List<Video>> callback) {
    videoDataSource.getPopularSongs(endpoint,
            new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<SongParameters>> t =
                        new GenericTypeIndicator<List<SongParameters>>() {
                        };
                List<SongParameters> songParametersList = dataSnapshot.getValue(t);
                callback.onLoaded(songParamentersMapper.toModel(songParametersList));
              }

              @Override
              public void onCancelled(FirebaseError firebaseError) {
                callback.onError();
              }
            });
  }

  @Override public void saveDownloadedItem(String endpoint, SongParameters songParameters) {
    videoDataSource.saveDownloadedItem(endpoint, songParameters);
  }
}
