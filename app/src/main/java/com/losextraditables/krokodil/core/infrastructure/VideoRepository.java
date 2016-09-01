package com.losextraditables.krokodil.core.infrastructure;

import com.losextraditables.krokodil.core.model.SearchItem;
import com.losextraditables.krokodil.core.model.Video;
import java.util.List;

public interface VideoRepository {

  List<Video> getPopularVideos();

  List<SearchItem> search(String query);
}
