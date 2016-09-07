package com.losextraditables.krokodil.android.models.mapper;

import com.losextraditables.krokodil.android.models.VideoModel;
import com.losextraditables.krokodil.core.model.Video;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

public class VideoModelMapper {

  @Inject public VideoModelMapper() {
  }

  public VideoModel toModel(Video video) {
    VideoModel videoModel = new VideoModel();
    videoModel.setThumbnails(video.getThumbnails());
    videoModel.setPublishedAt(video.getPublishedAt());
    videoModel.setDuration(video.getDuration());
    videoModel.setDescription(video.getDescription());
    videoModel.setId(video.getId());
    videoModel.setTitle(video.getTitle());
    videoModel.setViewCount(video.getViewCount());
    return videoModel;
  }

  public List<VideoModel> toModel(Collection<Video> bos) {
    List<VideoModel> models = null;
    if (bos != null && !bos.isEmpty()) {
      models = new ArrayList<>(bos.size());
      for (Video bo : bos) {
        models.add(toModel(bo));
      }
    }
    return models;
  }
}
