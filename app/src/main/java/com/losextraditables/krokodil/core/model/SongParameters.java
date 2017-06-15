package com.losextraditables.krokodil.core.model;

import lombok.Data;

@Data
public class SongParameters {
  public String thumbnailUrl;
  public String duration;
  public String title;
  public String author;
  public String visits;
  public String description;
  public String videoId;
  public Long timesDownloaded;
}
