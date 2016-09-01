package com.losextraditables.krokodil.android.infrastructure.data.models;

import java.util.Map;
import lombok.Data;

@Data
public class Thumbnail {
  private Map<String, ThumbnailParameters> thumbnails;
}
