package com.losextraditables.krokodil.android.infrastructure.data.models.search;

import com.losextraditables.krokodil.android.infrastructure.data.models.Snippet;
import lombok.Data;

@Data
public class SearchResponseEntity {
  private SearchIdResponse id;
  private Snippet snippet;
}
