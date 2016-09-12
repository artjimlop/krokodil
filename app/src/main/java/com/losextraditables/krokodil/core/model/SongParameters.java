package com.losextraditables.krokodil.core.model;

import lombok.Data;

@Data
public class SongParameters {
  private String thumbnailUrl;
  private String duration;
  private String title;
  private String author;
  private String visits;
  private String description;
  private String videoId;
  private Long timesDownloaded;
}
