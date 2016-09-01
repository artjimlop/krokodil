package com.losextraditables.krokodil.android.infrastructure.data.models;

import java.util.Date;
import java.util.Map;
import lombok.Data;

@Data
public class Snippet {
  private Date publishedAt;
  private String title;
  private String description;
  private Map<String, ThumbnailParameters> thumbnails;
  //TODO private List<Thumbnail> thumbnails;
}
