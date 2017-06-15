package com.losextraditables.krokodil.core.infrastructure;

import com.firebase.client.ValueEventListener;
import com.losextraditables.krokodil.core.model.SearchItem;
import com.losextraditables.krokodil.core.model.SongParameters;
import com.losextraditables.krokodil.core.model.Video;

import java.util.List;

public interface VideoRepository {

  List<Video> getPopularVideos();

  List<SearchItem> search(String query);

  List<Video> getVideos(List<String> videoIds);

  void getDiscoveredVideos(String endpoint,
                           ValueEventListener listener);

  void saveDownloadedItem(String endpoint, SongParameters songParameters);
}
