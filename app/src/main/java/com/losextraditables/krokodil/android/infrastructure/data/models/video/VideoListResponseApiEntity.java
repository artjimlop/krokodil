package com.losextraditables.krokodil.android.infrastructure.data.models.video;

import java.util.List;
import lombok.Data;

@Data
public class VideoListResponseApiEntity {
  private List<VideoApiEntity> items;
}
