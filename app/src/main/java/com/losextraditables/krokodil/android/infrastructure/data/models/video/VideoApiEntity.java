package com.losextraditables.krokodil.android.infrastructure.data.models.video;

import com.losextraditables.krokodil.android.infrastructure.data.models.Snippet;
import lombok.Data;

@Data
public class VideoApiEntity {
  private String id;
  private Snippet snippet;
  private Statistics statistics;
  private ContentDetails contentDetails;
}
