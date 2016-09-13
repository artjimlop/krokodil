package com.losextraditables.krokodil.android.infrastructure.data.models.search;

import java.util.List;
import lombok.Data;

@Data
public class SearchListResponseApiEntity {
  List<SearchResponseEntity> items;
}
