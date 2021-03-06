/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.data.repositories;

import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchResponseEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoApiEntity;
import java.util.List;

public interface VideoDataSource {

  List<VideoApiEntity> getPopulars();

  List<SearchResponseEntity> search(String query);

  List<VideoApiEntity> getVideosById(List<String> videoIds);
}
