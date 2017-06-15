package com.losextraditables.krokodil.core.infrastructure;

import com.losextraditables.krokodil.core.actions.Action;
import com.losextraditables.krokodil.core.model.SearchItem;
import com.losextraditables.krokodil.core.model.SongParameters;
import com.losextraditables.krokodil.core.model.Video;

import java.util.List;

public interface VideoRepository {

  List<Video> getPopularVideos();

  List<SearchItem> search(String query);

  List<Video> getVideos(List<String> videoIds);

  void getDiscoveredVideos(String endpoint,
                           Action.Callback<List<SongParameters>> callback);

  void saveDownloadedItem(String endpoint, SongParameters songParameters);
}
