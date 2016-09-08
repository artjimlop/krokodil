/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.data.repositories;

import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchListResponseApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchResponseEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoListResponseApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.net.services.VideoApiService;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import java.io.IOException;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Response;

public class RetrofitVideoDataSource implements VideoDataSource {

  private VideoApiService videoApiService;

  @Inject
  public RetrofitVideoDataSource(VideoApiService videoApiService) {
    this.videoApiService = videoApiService;
  }

  @Override public List<VideoApiEntity> getPopulars() {
    try {
      Call<VideoListResponseApiEntity> call = videoApiService.getPopularVideos();
      Response<VideoListResponseApiEntity> response = call.execute();
      return response.body().getItems();
    } catch (IOException e) {
      throw new ServerCommunicationException(e);
    }
  }

  @Override public List<SearchResponseEntity> search(String query) {
    try {
      Call<SearchListResponseApiEntity> call = videoApiService.searchVideos(query);
      Response<SearchListResponseApiEntity> response = call.execute();
      return response.body().getItems();
    } catch (IOException e) {
      throw new ServerCommunicationException(e);
    }
  }

  @Override public List<VideoApiEntity> getVideosById(List<String> videoIds) {
    try {
      String ids = "";
      for (int i = 0; i < videoIds.size(); i++) {
        if (i < videoIds.size() - 1) {
          ids += videoIds.get(i) + ",";
        } else {
          ids += videoIds.get(i);
        }
      }
      Call<VideoListResponseApiEntity> call = videoApiService.getVideos(ids);
      Response<VideoListResponseApiEntity> response = call.execute();
      return response.body().getItems();
    } catch (IOException e) {
      throw new ServerCommunicationException(e);
    }
  }
}
