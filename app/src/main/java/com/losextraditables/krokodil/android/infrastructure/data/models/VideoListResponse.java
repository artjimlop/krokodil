package com.losextraditables.krokodil.android.infrastructure.data.models;

import java.util.List;
import lombok.Data;

@Data
public class VideoListResponse {
  List<VideoEntity> items;
}
