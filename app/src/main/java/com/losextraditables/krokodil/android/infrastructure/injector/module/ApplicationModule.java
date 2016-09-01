/**
 * Copyright (C) 2016 Arturo Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.losextraditables.krokodil.android.infrastructure.injector.module;

import android.content.Context;
import com.losextraditables.krokodil.AndroidApplication;
import com.losextraditables.krokodil.UIThread;
import com.losextraditables.krokodil.android.infrastructure.data.net.ApiConstants;
import com.losextraditables.krokodil.android.infrastructure.data.net.interceptor.AuthInterceptor;
import com.losextraditables.krokodil.android.infrastructure.data.net.services.VideoApiService;
import com.losextraditables.krokodil.android.infrastructure.data.repositories.RetrofitVideoDataSource;
import com.losextraditables.krokodil.android.infrastructure.data.repositories.VideoDataSource;
import com.losextraditables.krokodil.android.infrastructure.data.repositories.VideoRepositoryImpl;
import com.losextraditables.krokodil.core.infrastructure.VideoRepository;
import com.losextraditables.krokodil.core.infrastructure.executor.JobExecutor;
import com.losextraditables.krokodil.core.infrastructure.executor.PostExecutionThread;
import com.losextraditables.krokodil.core.infrastructure.executor.ThreadExecutor;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class ApplicationModule {

  private final AndroidApplication application;

  public ApplicationModule(AndroidApplication application) {
    this.application = application;
  }

  @Provides @Singleton Context provideContext() {
    return application;
  }

  @Provides @Singleton ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
    return jobExecutor;
  }

  @Provides @Singleton PostExecutionThread providePostExecutionThread(UIThread uiThread) {
    return uiThread;
  }

  @Provides
  @Singleton
  @Named("public_key")
  String providePublicKey() {
    return application.getYoutubeApiKey();
  }

  @Provides @Singleton VideoApiService provideVideoApiService(AuthInterceptor authInterceptor) {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build();

    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiConstants.BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    return retrofit.create(VideoApiService.class);
  }

  @Provides @Singleton VideoRepository provideVideoRepository(VideoRepositoryImpl videoRepository) {
    return videoRepository;
  }

  @Provides @Singleton VideoDataSource provideVideoDataSource(
      RetrofitVideoDataSource retrofitVideoDataSource) {
    return retrofitVideoDataSource;
  }
}
