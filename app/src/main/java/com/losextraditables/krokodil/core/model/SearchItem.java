package com.losextraditables.krokodil.core.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class SearchItem {
  private String videoId;
  private Date publishedAt;
  private String title;
  private String description;
  private List<String> thumbnails;
}
