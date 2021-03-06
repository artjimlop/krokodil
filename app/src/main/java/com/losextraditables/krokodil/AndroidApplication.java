/**
 * Copyright (C) 2016 Sergi Castillo Open Source Project <p/> Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at <p/> http://www.apache.org/licenses/LICENSE-2.0 <p/> Unless
 * required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.losextraditables.krokodil;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.losextraditables.krokodil.android.infrastructure.injector.component.ApplicationComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.component.DaggerApplicationComponent;
import com.losextraditables.krokodil.android.infrastructure.injector.module.ApplicationModule;
import com.losextraditables.krokodil.android.infrastructure.tools.CrashReportTool;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.inject.Inject;

public class AndroidApplication extends Application {

  // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
  private String firebaseEndpoint;
  private String twitterKey;
  private String twitterSecret;
  private String youtubeApiKey;

  @Inject CrashReportTool crashReportTool;

  private ApplicationComponent component;

  @Override
  public void onCreate() {
    super.onCreate();
    initializeProperties();
    initializeInjector();
    initializeBugDetector();
    Dexter.initialize(this);
  }

  private void initializeProperties() {
    try {
      Properties properties = new Properties();
      InputStream inputStream = getAssets().open("krokodil.properties");
      properties.load(inputStream);
      firebaseEndpoint = properties.getProperty("firebase_endpoint");
      twitterKey = properties.getProperty("twitter_key");
      twitterSecret = properties.getProperty("twitter_secret");
      youtubeApiKey = properties.getProperty("youtube_api_key");
    } catch (IOException e) {
            /* this must never go wrong*/
    }
  }

  private void initializeBugDetector() {
    TwitterAuthConfig authConfig = new TwitterAuthConfig(getTwitterKey(), getTwitterSecret());
    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics(), new Twitter(authConfig));
    } else {
      Fabric.with(this, new Crashlytics());
    }
  }

  private void initializeInjector() {
    component = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this))
        .build();
  }

  public ApplicationComponent getComponent() {
    return component;
  }

  public String getFirebaseEndpoint() {
    return firebaseEndpoint;
  }

  public String getTwitterKey() {
    return twitterKey;
  }

  public String getTwitterSecret() {
    return twitterSecret;
  }

  public String getYoutubeApiKey() {
    return youtubeApiKey;
  }
}
