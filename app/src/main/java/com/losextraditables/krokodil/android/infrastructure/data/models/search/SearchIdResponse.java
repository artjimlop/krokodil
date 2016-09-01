package com.losextraditables.krokodil.android.infrastructure.data.models.search;

import lombok.Data;

@Data
public class SearchIdResponse {
  private String kind;
  private String videoId;
  private String channelId;
  private String playlistId;
}
