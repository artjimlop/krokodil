package com.losextraditables.krokodil.android.infrastructure.data.models.mappers;

import com.losextraditables.krokodil.android.infrastructure.data.models.ThumbnailParameters;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoApiEntity;
import com.losextraditables.krokodil.core.model.Video;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

public class VideoEntityMapper {

  @Inject public VideoEntityMapper() {
  }

  public Video toModel(VideoApiEntity videoEntity) {
    Map<String, ThumbnailParameters> thumbnails = videoEntity.getSnippet().getThumbnails();
    List<String> thumbnailUrls = new ArrayList<>();
    Set<String> keys = thumbnails.keySet();
    for (String key : keys) {
      thumbnailUrls.add(thumbnails.get(key).getUrl());
    }

    Video video;
    if (videoEntity.getContentDetails() != null && videoEntity.getStatistics() != null) {
        video = new Video(
              videoEntity.getId(),
              videoEntity.getContentDetails().getDuration(),
              videoEntity.getSnippet().getPublishedAt(),
              videoEntity.getSnippet().getTitle(),
              videoEntity.getSnippet().getDescription(),
              thumbnailUrls,
              videoEntity.getStatistics().getViewCount(),
              videoEntity.getSnippet().getChannelTitle()
      );
    } else {
      video = new Video(
              videoEntity.getId(),
              "",
              videoEntity.getSnippet().getPublishedAt(),
              videoEntity.getSnippet().getTitle(),
              videoEntity.getSnippet().getDescription(),
              thumbnailUrls,
              0L,
              videoEntity.getSnippet().getChannelTitle()
      );
    }

    return video;
  }

  public List<Video> toModel(Collection<VideoApiEntity> bos) {
    List<Video> models = null;
    if (bos != null && !bos.isEmpty()) {
      models = new ArrayList<>(bos.size());
      for (VideoApiEntity bo : bos) {
        models.add(toModel(bo));
      }
    }
    return models;
  }
}
