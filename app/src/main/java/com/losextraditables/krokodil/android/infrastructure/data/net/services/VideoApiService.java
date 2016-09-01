/**
 * Copyright (C) 2016 Arturo Open Source Project <p> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p> http://www.apache.org/licenses/LICENSE-2.0 <p> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.data.net.services;

import com.losextraditables.krokodil.android.infrastructure.data.models.VideoListResponse;
import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchListResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VideoApiService {

  @GET("videos?chart=mostPopular&part=snippet&maxResults=50")
  Call<VideoListResponse> getPopularVideos();

  @GET("search?part=snippet&type=video&maxResults=50") Call<SearchListResponse> searchVideos(
      @Query("q") String query);
}
