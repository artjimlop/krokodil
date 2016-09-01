package com.losextraditables.krokodil.android.models;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class VideoModel {
  private String id;
  private String duration;
  private Date publishedAt;
  private String title;
  private String description;
  private List<String> thumbnails;
}
