package com.losextraditables.krokodil.android.models.mapper;

import com.losextraditables.krokodil.android.models.SearchItemModel;
import com.losextraditables.krokodil.core.model.SearchItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

public class SearchModelMapper {

  @Inject public SearchModelMapper() {
  }

  public SearchItemModel toModel(SearchItem bo) {
    SearchItemModel searchItemModel = new SearchItemModel();
    searchItemModel.setVideoId(bo.getVideoId());
    searchItemModel.setTitle(bo.getTitle());
    searchItemModel.setThumbnails(bo.getThumbnails());
    searchItemModel.setPublishedAt(bo.getPublishedAt());
    searchItemModel.setDescription(bo.getDescription());
    return searchItemModel;
  }

  public List<SearchItemModel> toModel(Collection<SearchItem> bos) {
    List<SearchItemModel> models = null;
    if (bos != null && !bos.isEmpty()) {
      models = new ArrayList<>(bos.size());
      for (SearchItem bo : bos) {
        models.add(toModel(bo));
      }
    }
    return models;
  }
}
