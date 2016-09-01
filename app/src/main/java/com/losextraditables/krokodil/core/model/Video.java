package com.losextraditables.krokodil.core.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Video {
  private String id;
  private String duration;
  private Date publishedAt;
  private String title;
  private String description;
  private List<String> thumbnails;
}
