package com.losextraditables.krokodil.android.models;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class SearchItemModel {
  private String videoId;
  private Date publishedAt;
  private String title;
  private String description;
  private List<String> thumbnails;
}
