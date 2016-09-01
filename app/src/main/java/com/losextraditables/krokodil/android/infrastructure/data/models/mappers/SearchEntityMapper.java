package com.losextraditables.krokodil.android.infrastructure.data.models.mappers;

import com.losextraditables.krokodil.android.infrastructure.data.models.ThumbnailParameters;
import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchResponseEntity;
import com.losextraditables.krokodil.core.model.SearchItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

public class SearchEntityMapper {
  @Inject public SearchEntityMapper() {
  }

  public SearchItem toModel(SearchResponseEntity entity) {
    SearchItem searchItem = new SearchItem();
    searchItem.setDescription(entity.getSnippet().getDescription());
    searchItem.setPublishedAt(entity.getSnippet().getPublishedAt());
    searchItem.setTitle(entity.getSnippet().getTitle());
    searchItem.setVideoId(entity.getId().getVideoId());

    Map<String, ThumbnailParameters> thumbnails = entity.getSnippet().getThumbnails();
    Set<String> keySet = thumbnails.keySet();
    List<String> urls = new ArrayList<>(keySet.size());
    for (String key : keySet) {
      urls.add(thumbnails.get(key).getUrl());
    }
    searchItem.setThumbnails(urls);

    return searchItem;
  }

  public List<SearchItem> toModel(Collection<SearchResponseEntity> bos) {
    List<SearchItem> models = null;
    if (bos != null && !bos.isEmpty()) {
      models = new ArrayList<>(bos.size());
      for (SearchResponseEntity bo : bos) {
        models.add(toModel(bo));
      }
    }
    return models;
  }
}
