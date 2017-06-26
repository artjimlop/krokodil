/**
 * Copyright (C) 2016 Arturo Open Source Project <p/> Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the License. You may obtain
 * a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.data.repositories;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.ValueEventListener;
import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchListResponseApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.search.SearchResponseEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.models.video.VideoListResponseApiEntity;
import com.losextraditables.krokodil.android.infrastructure.data.net.services.VideoApiService;
import com.losextraditables.krokodil.core.infrastructure.exception.ServerCommunicationException;
import com.losextraditables.krokodil.core.model.SongParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class RetrofitVideoDataSource implements VideoDataSource {

  public static final String DISCOVER = "discover";
  private VideoApiService videoApiService;
  private boolean changeMade = false;

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
      Integer from = videoIds.size() - 45;
      Integer to  = videoIds.size() - 1;
      List<String> lastIds = videoIds.subList(from, to);
      String ids = "";
      for (int i = 0; i < lastIds.size(); i++) {
        if (i < lastIds.size() - 1) {
          ids += lastIds.get(i) + ",";
        } else {
          ids += lastIds.get(i);
        }
      }
      Call<VideoListResponseApiEntity> call = videoApiService.getVideos(ids);
      Response<VideoListResponseApiEntity> response = call.execute();
      return response.body().getItems();
    } catch (IOException e) {
      throw new ServerCommunicationException(e);
    }
  }

  @Override public void saveDownloadedItem(String endpoint, SongParameters songParameters) {
    changeMade = false;
    Firebase firebase = new Firebase(endpoint);
    Firebase discover = firebase.child(DISCOVER);
    discover.addValueEventListener(new ValueEventListener() {
      @Override public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<List<SongParameters>> t =
            new GenericTypeIndicator<List<SongParameters>>() {
            };
        List<SongParameters> songParametersList = dataSnapshot.getValue(t);
        List<SongParameters> modifiedList = new ArrayList<>();
        boolean contained = false;
        if (!changeMade) {
          if (songParametersList != null) {
            for (SongParameters parameters : songParametersList) {
              if (parameters.getVideoId().equals(songParameters.getVideoId())) {
                contained = true;
                parameters.setTimesDownloaded(parameters.getTimesDownloaded() + 1);
                modifiedList.add(parameters);
              } else {
                modifiedList.add(parameters);
              }
            }
            if (!contained) {
              songParameters.setTimesDownloaded(1L);
              modifiedList.add(songParameters);
            }
            discover.setValue(modifiedList);
          } else {
            songParameters.setTimesDownloaded(1L);
            songParametersList = Collections.singletonList(songParameters);
            discover.setValue(songParametersList);
          }
          changeMade = true;
        }
      }

      @Override public void onCancelled(FirebaseError firebaseError) {
        //TODO something
      }
    });
  }

  @Override public void getPopularSongs(String endpoint,
                                       ValueEventListener listener) {
    Firebase firebase = new Firebase(endpoint);
    Firebase discover = firebase.child(DISCOVER);
    discover.addValueEventListener(listener);
  }
}
